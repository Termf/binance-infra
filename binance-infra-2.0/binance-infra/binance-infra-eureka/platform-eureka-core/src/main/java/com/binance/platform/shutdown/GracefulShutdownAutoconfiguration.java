package com.binance.platform.shutdown;

import javax.servlet.Servlet;

import com.binance.platform.tomcat.ManagementTomcatCustomizer;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http2.Http2Protocol;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.binance.platform.compress.GzipSwitchTomcatConnectCustomizer;
import com.binance.platform.tomcat.NoSessionManager;
import com.netflix.discovery.EurekaClient;

@Configuration
public class GracefulShutdownAutoconfiguration {

    @Configuration
    @ConditionalOnClass({Servlet.class, Server.class, Loader.class, WebAppContext.class})
    public static class JettyGracefulShutdownAutoconfiguration {

        @Bean
        public GracefulShutdownJetty jettyGracefulShutdown(Environment env, EurekaClient eurekaClient) {
            return new GracefulShutdownJetty(env, eurekaClient);
        }

        @Bean
        public WebServerFactoryCustomizer<JettyServletWebServerFactory>
            jettyEmbeddedServletContainerFactory(GracefulShutdownJetty gracefulShutdownJetty) {
            return container -> {
                if (container instanceof JettyServletWebServerFactory) {
                    ((JettyServletWebServerFactory)container).addServerCustomizers(gracefulShutdownJetty);
                }
            };
        }
    }

    @Configuration
    @ConditionalOnClass({Servlet.class, Tomcat.class})
    public static class TomcatGracefulShutdownAutoconfiguration {

        @Bean
        public GracefulShutdownTomcat gracefulShutdownTomcat(Environment env, EurekaClient eurekaClient) {
            return new GracefulShutdownTomcat(env, eurekaClient);
        }

        @Bean
        public GzipSwitchTomcatConnectCustomizer tomcatGzipSwitch() {
            return new GzipSwitchTomcatConnectCustomizer();
        }

        @Bean
        public ManagementTomcatCustomizer managementTomcatCustomizer() {
            return new ManagementTomcatCustomizer();
        }

        @Bean
        public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer(Environment env,
            GracefulShutdownTomcat gracefulShutdownTomcat,
            GzipSwitchTomcatConnectCustomizer gzipSwitchTomcatConnectCustomizer,
            @Autowired(required = false) ServerProperties serverProperties) {
            return container -> {
                if (container instanceof TomcatServletWebServerFactory) {
                    TomcatServletWebServerFactory tomcatServletWebServerFactory =
                        (TomcatServletWebServerFactory)container;
                    // GzipSwitchTomcatConnectCustomizer必须在添加Protocol的Customizer后面，否则新增加的协议启动时不会打开gzip压缩
                    tomcatServletWebServerFactory.addConnectorCustomizers(gracefulShutdownTomcat,
                        new TomcatConnectorCustomizer() {
                            private final Logger LOGGER = LoggerFactory.getLogger(TomcatConnectorCustomizer.class);

                            @Override
                            public void customize(Connector connector) {

                                String keepAliveTimeout =
                                        env.getProperty("server.tomcat.keepAlive.keepAliveTimeout", "60000");

                                // http1.1
                                AbstractHttp11Protocol http1Protocol =
                                    (AbstractHttp11Protocol)connector.getProtocolHandler();
                                http1Protocol.setRejectIllegalHeader(false);
                                http1Protocol.setRelaxedQueryChars("[]|{}^&#x5c;&#x60;&quot;&lt;&gt;&amp;");
                                http1Protocol.setRelaxedPathChars("[]|{}^&#x5c;&#x60;&quot;&lt;&gt;&amp;");
                                String enableKeepalive = env.getProperty("server.tomcat.keepAlive.enable", "false");
                                if (BooleanUtils.toBoolean(enableKeepalive)) {
                                    String maxKeepAliveRequests =
                                        env.getProperty("server.tomcat.keepAlive.maxKeepAliveRequests", "10000");
                                    // 默认是60秒,设置60秒内没有请求则服务端自动断开keepalive链接
                                    http1Protocol.setKeepAliveTimeout(Integer.valueOf(keepAliveTimeout));
                                    // 默认是100，当客户端发送超过3000个请求则自动断开keepalive链接
                                    http1Protocol.setMaxKeepAliveRequests(Integer.valueOf(maxKeepAliveRequests));
                                }
                                LOGGER.info("Tomcat ==> maxKeepAliveRequests {}",
                                    http1Protocol.getMaxKeepAliveRequests());
                                LOGGER.info("Tomcat ==> keepaliveTimeout: {} ms", http1Protocol.getKeepAliveTimeout());
                                LOGGER.info("Tomcat ==> connectionTimeout: {} ms",
                                    http1Protocol.getConnectionTimeout());
                                LOGGER.info("Tomcat ==> maxConnections: {}", http1Protocol.getMaxConnections());

                                // http2
                                Boolean http2Enabled = env.getProperty("server.tomcat.http2.enabled", Boolean.class, true);
                                if (http2Enabled) {
                                    Http2Protocol http2Protocol = new Http2Protocol();
                                    if (serverProperties != null) {
                                        int maxHeaderSize = (int) serverProperties.getMaxHttpHeaderSize().toBytes();
                                        http2Protocol.setMaxHeaderSize(maxHeaderSize);
                                    }
                                    // 客户端配置的maxConcurrentStreams只用于建立连接时的并发控制，最终的复用量由服务端在协商时发送给客户端
                                    String maxHttp2ConcurrentStreams =
                                            env.getProperty("server.tomcat.http2.maxConcurrentStreams", "100");
                                    http2Protocol.setMaxConcurrentStreams(Long.parseLong(maxHttp2ConcurrentStreams));
                                    String maxConcurrentStreamExecution =
                                            env.getProperty("server.tomcat.http2.maxConcurrentStreamExecution", "20");
                                    http2Protocol
                                            .setMaxConcurrentStreamExecution(Integer.parseInt(maxConcurrentStreamExecution));
                                    http2Protocol.setKeepAliveTimeout(Integer.valueOf(keepAliveTimeout));
                                    connector.addUpgradeProtocol(http2Protocol);
                                    LOGGER.info("Tomcat ==> Http2 keepaliveTimeout: {} ms",
                                            http2Protocol.getKeepAliveTimeout());
                                }
                            }
                        }, gzipSwitchTomcatConnectCustomizer);
                    String enableSession = env.getProperty("spring.application.enablesession", "false");
                    if (!BooleanUtils.toBoolean(enableSession)) {
                        tomcatServletWebServerFactory.addContextCustomizers(new TomcatContextCustomizer() {

                            @Override
                            public void customize(Context context) {
                                context.setManager(new NoSessionManager());
                            }

                        });
                    }

                }
            };
        }

    }

}
