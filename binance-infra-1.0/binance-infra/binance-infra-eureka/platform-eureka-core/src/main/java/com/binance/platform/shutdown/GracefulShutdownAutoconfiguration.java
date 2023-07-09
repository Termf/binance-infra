package com.binance.platform.shutdown;

import javax.servlet.Servlet;

import org.apache.catalina.startup.Tomcat;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.netflix.discovery.EurekaClient;

@Configuration
public class GracefulShutdownAutoconfiguration {

    @Configuration
    @ConditionalOnClass({Servlet.class, Server.class, Loader.class, WebAppContext.class})
    public static class JettyGracefulShutdownAutoconfiguration {

        @Bean
        public GracefulShutdownJetty jettyGracefulShutdown(Environment env,
            JettyEmbeddedServletContainerFactory jettyfactory, EurekaClient eurekaClient) {
            GracefulShutdownJetty gracefulShutdownJetty = new GracefulShutdownJetty(env, eurekaClient);
            jettyfactory.addServerCustomizers(gracefulShutdownJetty);
            return gracefulShutdownJetty;
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
        public EmbeddedServletContainerCustomizer tomcatCustomizer(GracefulShutdownTomcat gracefulShutdownTomcat) {
            return container -> {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    ((TomcatEmbeddedServletContainerFactory)container).addConnectorCustomizers(gracefulShutdownTomcat);
                }
            };
        }

    }

}
