package com.binance.platform.eureka.wrapper;

import static com.binance.platform.EurekaConstants.EUREKA_METADATA_ENVKEY;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GROUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_MANAGEMENT_URL;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_VERSION;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_ENVKEY;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GRAYFLOW;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GROUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_VERSION;
import static com.binance.platform.EurekaConstants.SPRING_BOOT_VERSION;
import static com.binance.platform.EurekaConstants.*;

import org.springframework.boot.SpringBootVersion;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

public class EurekaServiceRegistryWrapper extends EurekaServiceRegistry {

    private final ServiceRegistry<EurekaRegistration> serviceRegistry;
    private final ConfigurableEnvironment environment;

    public EurekaServiceRegistryWrapper(ServiceRegistry<EurekaRegistration> serviceRegistry,
        ConfigurableEnvironment environment) {
        this.serviceRegistry = serviceRegistry;
        this.environment = environment;
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

    @Override
    public Object getStatus(EurekaRegistration registration) {
        return serviceRegistry.getStatus(registration);
    }

    @Override
    public void register(EurekaRegistration registration) {
        String group = this.getProperties(PROVIDER_INSTANCE_GROUP);
        String version = this.getProperties(PROVIDER_INSTANCE_VERSION);
        String envKey = this.getProperties(PROVIDER_INSTANCE_ENVKEY);
        String grayflow = this.getProperties(PROVIDER_INSTANCE_GRAYFLOW);
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
        registration.getMetadata().put(EUREKA_METADATA_MANAGEMENT_URL, getManagementUrl(registration));
        registration.getMetadata().put(SPRING_BOOT_VERSION, SpringBootVersion.getVersion());
        serviceRegistry.register(registration);
        new ScheduleReportServerLoad(registration, environment).start();
    }

    @Override
    public void setStatus(EurekaRegistration registration, String status) {
        serviceRegistry.setStatus(registration, status);
    }

}
