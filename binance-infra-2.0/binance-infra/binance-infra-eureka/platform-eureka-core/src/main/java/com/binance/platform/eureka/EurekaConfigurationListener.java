package com.binance.platform.eureka;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import com.binance.platform.compress.GzipSwitchTomcatConnectCustomizer;

@Order(value = 2)
public class EurekaConfigurationListener implements SpringApplicationRunListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaConfigurationListener.class);

    private static final ConcurrentHashMap<String, Object> DEFAULTPROPERTIES = new ConcurrentHashMap<>();

    public EurekaConfigurationListener(SpringApplication application, String[] args) {}

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        // 最大重试次数
        DEFAULTPROPERTIES.put("ribbon.CustomizeMaxAutoRetries", 0);
        // 这个重试是retrySameServer
        DEFAULTPROPERTIES.put("ribbon.MaxAutoRetries", 0);
        // 这个重试是retryNextServer
        DEFAULTPROPERTIES.put("ribbon.MaxAutoRetriesNextServer", 0);
        DEFAULTPROPERTIES.put("ribbon.ReadTimeout", 4000);
        DEFAULTPROPERTIES.put("ribbon.ConnectTimeout", 1000);
        DEFAULTPROPERTIES.put("ribbon.OkToRetryOnAllOperations", false);
        DEFAULTPROPERTIES.put("eureka.client.healthcheck.enabled", true);
        DEFAULTPROPERTIES.put("eureka.client.initial-instance-info-replication-interval-seconds", 60);
        DEFAULTPROPERTIES.put("eureka.client.instance-info-replication-interval-seconds", 15);
        DEFAULTPROPERTIES.put("eureka.client.registry-fetch-interval-seconds", 15);
        DEFAULTPROPERTIES.put("eureka.instance.prefer-ip-address", true);
        DEFAULTPROPERTIES.put("server.max-http-header-size", "16KB");
        DEFAULTPROPERTIES.put("server.tomcat.max-connections", "50000");
        DEFAULTPROPERTIES.put("server.tomcat.accept-count", "800");// 接受排队的请求个数，一般和最大工作线程数一致
        DEFAULTPROPERTIES.put("server.tomcat.max-threads", "800");// 最大工作线程数
        DEFAULTPROPERTIES.put("server.tomcat.min-spare-threads", "200");// 最小工作空闲线程数
        DEFAULTPROPERTIES.put("eureka.instance.instance-id", environment.getProperty("spring.cloud.client.ip-address")
            + ":" + environment.getProperty("server.port", "8080"));

        // gzip
        DEFAULTPROPERTIES.put("feign.compression.response.enabled", "true");
        DEFAULTPROPERTIES.put("server.compression.mime-types",
            GzipSwitchTomcatConnectCustomizer.COMPRESSIBLE_MIME_TYPE);

        // 连接池最大为500
        DEFAULTPROPERTIES.put("feign.httpclient.max-connections", 500);
        // 对每一个节点最大路由树为200
        DEFAULTPROPERTIES.put("feign.httpclient.max-connections-per-route", 200);
        // 连接的keepalive设置为60s
        DEFAULTPROPERTIES.put("feign.httpclient.time-to-live", 60);

        // http2
        DEFAULTPROPERTIES.put("server.http2.enabled", "true");
        // okhttp disable/http client disable
        DEFAULTPROPERTIES.put("feign.httpclient.enabled", "false");
        DEFAULTPROPERTIES.put("feign.okhttp.enabled", "false");
        DEFAULTPROPERTIES.put("feign.jetty.enable", "true");

        Properties lastDefaultProps = new Properties();
        DEFAULTPROPERTIES.forEach((k, v) -> {
            if (!environment.containsProperty(k)) {
                lastDefaultProps.put(k, v);
            }
        });

        if (!lastDefaultProps.isEmpty()) {
            lastDefaultProps.put("server.compression.enabled", true);
            lastDefaultProps.put("server.compression.min-response-size", "2KB");
            logger.warn("Some dangerous properties was NOT set by user. Give it default value, default value:"
                + lastDefaultProps.toString());
            environment.getPropertySources()
                .addLast(new PropertiesPropertySource("EurekaDefaultPropertySources", lastDefaultProps));
        }
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {}

    @Override
    public void starting() {}

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {}

    @Override
    public void started(ConfigurableApplicationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        // TODO Auto-generated method stub

    }
}
