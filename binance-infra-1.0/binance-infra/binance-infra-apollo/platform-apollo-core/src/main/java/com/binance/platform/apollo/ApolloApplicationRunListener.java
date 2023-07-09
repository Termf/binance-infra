package com.binance.platform.apollo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.binance.platform.env.EnvUtil;
import com.binance.platform.springcloud.SpringCloudUtils;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;

@Order(value = 1)
public class ApolloApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApolloApplicationRunListener.class);
    private static final String APOLLO_APP_ID_KEY = "app.id";
    private static final String SPRINGBOOT_APPLICATION_NAME = "spring.application.name";
    private static final String SPRINGBOOT_PROFILES_ACTIVE = "spring.profiles.active";
    public static final String CONFIGCENTER_INFRA_NAMESPACE = "ops.infra.all";
    private static final Properties PRPERTIES_CACHE = new Properties();

    static {
        try {
            Resource[] resources =
                new PathMatchingResourcePatternResolver().getResources("classpath*:apollo-env.properties");
            for (Resource resource : resources) {
                PropertiesLoaderUtils.fillProperties(PRPERTIES_CACHE, resource);
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public ApolloApplicationRunListener(SpringApplication application, String[] args) {
        boolean isSpringCloudConfig = SpringCloudUtils.isSpringCloudConfig();
        if (!isSpringCloudConfig) {
            System.setProperty("spring.cloud.bootstrap.enabled", "false");
        }
        System.setProperty("spring.main.allow-bean-definition-overriding", "true");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        // 初始化环境
        initEnvAndMeta(environment);
        // 初始化appId
        initAppId(environment);
        // 初始化公共namespace的一些配置
        initInfraConfigFromPublicNameSpace(environment);
        // 从应用的namespace里加载一些配置
        initInfraConfigFromApplicationNameSpace(environment);
    }

    private void initEnvAndMeta(ConfigurableEnvironment environment) {
        String active = environment.getProperty(SPRINGBOOT_PROFILES_ACTIVE);
        String env = System.getProperty("env");
        if ((active == null && env == null) || EnvUtil.isDev()) {
            env = "dev";
        } else if ((active == null && env == null) || EnvUtil.isQa()) {
            env = "qa";
        } else {
            env = active != null ? active : env;
        }
        // 如果设置了spring.profiles.active，以这个为准
        System.setProperty("env", env.toUpperCase());
        // 再检查下apollo.meta和env_meta
        String apolloMeta = System.getProperty("apollo.meta");
        if (apolloMeta == null) {
            String meta = this.getMetaAddress(env);
            if (meta != null) {
                System.setProperty("apollo.meta", meta);
            }
        }
    }

    private void initAppId(ConfigurableEnvironment env) {
        String applicationName = env.getProperty(SPRINGBOOT_APPLICATION_NAME);
        String apolloAppId = env.getProperty(APOLLO_APP_ID_KEY);
        if (StringUtils.isEmpty(apolloAppId)) {
            if (!StringUtils.isEmpty(applicationName)) {
                System.setProperty(APOLLO_APP_ID_KEY, applicationName);
            } else {
                LOGGER
                    .warn("Config center must config app.id in " + DefaultApplicationProvider.APP_PROPERTIES_CLASSPATH);
            }
        } else {
            System.setProperty(APOLLO_APP_ID_KEY, apolloAppId);
        }
    }

    /**
     * 需要提前加载的配置
     */
    private void initInfraConfigFromApplicationNameSpace(ConfigurableEnvironment env) {
        System.setProperty("apollo.bootstrap.enabled", "true");
        System.setProperty("spring.banner.location", "META-INF/banner.txt");
        System.setProperty("eureka.instance.instance-id",
            env.getProperty("spring.cloud.client.ip-address") + ":" + env.getProperty("server.port", "8080"));
        Config frameworkConfig = ConfigService.getAppConfig();
        String kafkalogging = frameworkConfig.getProperty("management.logging.enable", null);
        String kafkaBoostrap = frameworkConfig.getProperty("managent.logs.kafka.bootstrapservers", null);
        String kafkaTopic = frameworkConfig.getProperty("managent.logs.kafka.topic", null);
        String loggingconfig = frameworkConfig.getProperty("logging.config", null);
        String logsql = frameworkConfig.getProperty("monitor.sql.log", null);
        if (kafkalogging != null)
            System.setProperty("management.logging.enable", kafkalogging);
        if (loggingconfig != null) {
            System.setProperty("logging.config", loggingconfig);
        }
        if (kafkaBoostrap != null) {
            System.setProperty("managent.logs.kafka.bootstrapservers", kafkaBoostrap);
        }
        if (kafkaTopic != null) {
            System.setProperty("managent.logs.kafka.topic", kafkaTopic);
        }
        if (logsql != null)
            System.setProperty("monitor.sql.log", logsql);

    }

    /**
     * 从公共的namespace中加载通用的配置
     */
    private void initInfraConfigFromPublicNameSpace(ConfigurableEnvironment env) {
        Config publicNamespaceConfig = ConfigService.getConfig(CONFIGCENTER_INFRA_NAMESPACE);
        Set<String> propertyNames = publicNamespaceConfig.getPropertyNames();
        if (propertyNames != null && propertyNames.size() > 0) {
            Properties propertes = new Properties();
            for (String propertyName : propertyNames) {
                propertes.setProperty(propertyName, publicNamespaceConfig.getProperty(propertyName, null));
            }
            String accessKey = publicNamespaceConfig.getProperty(SecretsManagerConstants.AWS_ACCESSKEY, null);
            String secretKey = publicNamespaceConfig.getProperty(SecretsManagerConstants.AWS_SECRETKEY, null);
            String region = publicNamespaceConfig.getProperty(SecretsManagerConstants.AWS_REGION, null);
            // 如果在开发环境，把accessKey和secretKey转化为spring cloud aws可用
            if (EnvUtil.isDev()) {
                if (accessKey != null) {
                    propertes.put("cloud.aws.credentials.accessKey", accessKey);
                }
                if (secretKey != null) {
                    propertes.put("cloud.aws.credentials.secretKey", secretKey);
                }
                if (region != null) {
                    propertes.put("cloud.aws.region.static", region);
                }
            }
            // 如果在容器环境，已经上了Aws环境，accessKey和secretKey要去除
            if (EnvUtil.isDocker()) {
                Iterator<?> iter = propertes.keySet().iterator();
                while (iter.hasNext()) {
                    String key = (String)iter.next();
                    if (StringUtils.containsIgnoreCase(key, "accessKey")
                        || StringUtils.containsIgnoreCase(key, "secretKey")) {
                        iter.remove();
                    }
                }
            }
            EnumerablePropertySource<?> enumerablePropertySource =
                new PropertiesPropertySource(CONFIGCENTER_INFRA_NAMESPACE, propertes);
            env.getPropertySources().addLast(enumerablePropertySource);
        }
    }

    @Override
    public void starting() {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    /**
     * help method
     */
    private String getMetaAddress(String env) {
        String meta = null;
        String newEnv = StringUtils.lowerCase(env);
        // 首先把spring.profile.active转化为小写看下有没取到meta地址
        if (StringUtils.equalsAnyIgnoreCase(env, "dev", "qa")) {
            meta = PRPERTIES_CACHE.getProperty(newEnv + ".meta");
        } else {
            meta = PRPERTIES_CACHE.getProperty(newEnv);
        }
        if (meta == null) {
            // 没取到把把中划线转化为下划线再取一次
            newEnv = StringUtils.replace(newEnv, "-", "_");
            meta = PRPERTIES_CACHE.getProperty(newEnv);
        }
        if (meta != null) {
            LOGGER.info("get meta address by spring.profile.active success,the meta address is " + meta);
        }
        return meta;
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
        // TODO Auto-generated method stub
        
    }

}
