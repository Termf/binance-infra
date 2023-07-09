package com.binance.platform.eureka;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

public class EurekaConfigurationListener implements SpringApplicationRunListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaConfigurationListener.class);

    private static final ConcurrentHashMap<String, Object> DEFAULTPROPERTIES = new ConcurrentHashMap<>();

    public EurekaConfigurationListener(SpringApplication application, String[] args) {}

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        // 最大重试次数
        DEFAULTPROPERTIES.put("ribbon.CustomizeMaxAutoRetries", 3);
        DEFAULTPROPERTIES.put("ribbon.MaxAutoRetries", 1);
        DEFAULTPROPERTIES.put("ribbon.MaxAutoRetriesNextServer", 1);
        DEFAULTPROPERTIES.put("ribbon.ReadTimeout", 3000);
        DEFAULTPROPERTIES.put("ribbon.ConnectTimeout", 3000);
        DEFAULTPROPERTIES.put("ribbon.OkToRetryOnAllOperations", false);
        DEFAULTPROPERTIES.put("eureka.client.healthcheck.enabled", true);
        DEFAULTPROPERTIES.put("eureka.client.instanceInfoReplicationIntervalSeconds", 10);
        DEFAULTPROPERTIES.put("eureka.instance.prefer-ip-address", true);
        DEFAULTPROPERTIES.put("eureka.instance.instance-id", environment.getProperty("spring.cloud.client.ip-address")
            + ":" + environment.getProperty("server.port", "8080"));
        Properties lastDefaultProps = new Properties();
        DEFAULTPROPERTIES.forEach((k, v) -> {
            if (!environment.containsProperty(k)) {
                lastDefaultProps.put(k, v);
            }
        });
        if (!lastDefaultProps.isEmpty()) {
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
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }

}
