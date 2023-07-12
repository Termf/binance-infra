package com.binance.platform.eureka.wrapper;

import static com.binance.platform.EurekaConstants.EUREKA_METADATA_ENVKEY;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GRAYFLOW;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GROUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_MANAGEMENT_URL;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_VERSION;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_WARMUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_ENVKEY;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GRAYFLOW;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GROUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_VERSION;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_WARMUP;
import static com.binance.platform.EurekaConstants.SPRING_BOOT_VERSION;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.env.EnvUtil;

public class EurekaServiceRegistryWrapper extends EurekaServiceRegistry {
    private final ServiceRegistry<EurekaRegistration> serviceRegistry;
    private final ConfigurableEnvironment environment;
    private final ConfigurableApplicationContext applicationContext;

    public EurekaServiceRegistryWrapper(ServiceRegistry<EurekaRegistration> serviceRegistry,
        ConfigurableApplicationContext applicationContext) {
        this.serviceRegistry = serviceRegistry;
        this.environment = applicationContext.getEnvironment();
        this.applicationContext = applicationContext;
    }

    @Override
    public void close() {
        serviceRegistry.close();
    }

    @Override
    public void deregister(EurekaRegistration registration) {
        serviceRegistry.deregister(registration);
    }

    private String getManagementUrl(EurekaRegistration registration) {
        String sslKey = "ssl.enabled";
        String portKey = "port";
        String pathKey = "context-path";
        String managementKey = "management.";
        if (environment.containsProperty(managementKey + portKey)) {
            sslKey = managementKey + sslKey;
            portKey = managementKey + portKey;
            pathKey = managementKey + pathKey;
        }
        Boolean isHttps = environment.getProperty(sslKey, Boolean.class, Boolean.FALSE);
        String port = environment.getProperty(portKey, "");
        String contextPath = environment.getProperty(pathKey, "/");
        String scheme = isHttps ? "https" : "http";
        String uri = String.format("%s://%s:%s", scheme, registration.getHost(),
            StringUtils.isEmpty(port) ? registration.getPort() : port);
        uri = uri + contextPath;
        return uri;
    }

    private String getProperties(String key) {
        return this.environment.getProperty(key);
    }

    private String getProperties(String key, String defaultValue) {
        return this.environment.getProperty(key, defaultValue);
    }

    @Override
    public Object getStatus(EurekaRegistration registration) {
        return serviceRegistry.getStatus(registration);
    }

    private String findEnvKey() {
        // 首先查找spring.application.envflag
        String envKey = systemPropertyOrEnv(PROVIDER_INSTANCE_ENVKEY, PROVIDER_INSTANCE_ENVKEY);
        // 如果spring.application.envflag为空，则查找eureka.instance.metadataMap.envflag
        if (envKey == null) {
            envKey = systemPropertyOrEnv("eureka.instance.metadataMap.envflag", "eureka.instance.metadataMap.envflag");
        }
        // 如果是在开发环境，并且是本机IP，强制使用本机名做为灰度标签
        if (EnvUtil.isDev() && envKey == null) {
            // 获取VPN的地址
            String userName = System.getProperty("user.name", "gray");
            if (EnvUtil.isMacOs()) {
                System.setProperty("eureka.instance.metadataMap.envflag", userName);
                envKey = userName;
            }
        }
        return envKey;
    }

    public String systemPropertyOrEnv(String propertyName, String envName) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null) {
            return propertyValue;
        } else {
            propertyValue = System.getenv(envName);
            return propertyValue;
        }
    }

    @Override
    public void register(EurekaRegistration registration) {
        String group = this.getProperties(PROVIDER_INSTANCE_GROUP);
        String version = this.getProperties(PROVIDER_INSTANCE_VERSION);
        String envKey = this.findEnvKey();
        String grayflow = this.getProperties(PROVIDER_INSTANCE_GRAYFLOW);
        String warmup = this.getProperties(PROVIDER_INSTANCE_WARMUP);
        if (group != null && version != null) {
            registration.getMetadata().put(EUREKA_METADATA_GROUP, group);
            registration.getMetadata().put(EUREKA_METADATA_VERSION, version);
        }
        if (envKey != null) {
            registration.getMetadata().put(EUREKA_METADATA_ENVKEY, envKey);
        }
        if (grayflow != null) {
            registration.getMetadata().put(EUREKA_METADATA_GRAYFLOW, grayflow);
        }
        if (warmup != null) {
            registration.getMetadata().put(EUREKA_METADATA_WARMUP, warmup);
        }
        registration.getMetadata().put(EUREKA_METADATA_MANAGEMENT_URL, getManagementUrl(registration));
        String springbootVersion = SpringBootVersion.getVersion();
        String jdkVersion = System.getProperty("java.version");
        String myVersion = springbootVersion + "-JDK" + jdkVersion;
        registration.getMetadata().put(SPRING_BOOT_VERSION, myVersion);
        if (!webServerStarted()) {
            String delayRegistry =
                this.getProperties("eureka.client.initial-instance-info-replication-interval-seconds", "60");
            Integer delayRegistryInt = Integer.valueOf(delayRegistry) * 2 / 3;
            new Timer(true).schedule(new TimerTask() {
                @Override
                public void run() {
                    serviceRegistry.register(registration);
                    new ScheduleReportServerLoad(registration, environment).start();
                }
            }, delayRegistryInt * 1000);
        } else {
            serviceRegistry.register(registration);
            new ScheduleReportServerLoad(registration, environment).start();
        }

    }

    @Override
    public void setStatus(EurekaRegistration registration, String status) {
        serviceRegistry.setStatus(registration, status);
    }

    private boolean webServerStarted() {
        if (this.applicationContext instanceof ServletWebServerApplicationContext) {
            ServletWebServerApplicationContext servletWebServerApplicationContext =
                (ServletWebServerApplicationContext)this.applicationContext;
            WebServer webServer = servletWebServerApplicationContext.getWebServer();
            String className = webServer.getClass().getName();
            if (StringUtils.containsIgnoreCase(className, "tomcat")) {
                TomcatWebServer tomcat = (TomcatWebServer)webServer;
                Field startField = ReflectionUtils.findField(TomcatWebServer.class, "started");
                ReflectionUtils.makeAccessible(startField);
                Boolean started = (Boolean)ReflectionUtils.getField(startField, tomcat);
                return started;

            } else if (StringUtils.containsIgnoreCase(className, "jetty")) {
                JettyWebServer jetty = (JettyWebServer)webServer;
                Field startField = ReflectionUtils.findField(JettyWebServer.class, "started");
                ReflectionUtils.makeAccessible(startField);
                Boolean started = (Boolean)ReflectionUtils.getField(startField, jetty);
                return started;
            }
        }
        return true;
    }

}
