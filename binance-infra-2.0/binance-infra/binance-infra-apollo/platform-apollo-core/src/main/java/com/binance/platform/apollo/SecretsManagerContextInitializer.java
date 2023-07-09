package com.binance.platform.apollo;

import static com.binance.platform.apollo.SecretsManagerConstants.SECRETSMANAGER_PROPERTY_SOURCE_NAME;
import static com.ctrip.framework.apollo.enums.PropertyChangeType.MODIFIED;
import static com.ctrip.framework.apollo.spring.config.PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.property.PlaceholderHelper;
import com.ctrip.framework.apollo.spring.property.SpringValue;
import com.ctrip.framework.apollo.spring.property.SpringValueRegistry;
import com.ctrip.framework.apollo.spring.util.SpringInjector;
import com.google.common.collect.Lists;

/**
 * SecretsManagerContextInitializer需要比ApolloApplicationContextInitializer优先级低一级，这样的话region、accessKey、secretKey可以配置在apollo里
 * 而且的话可以把从SecretsManager取出的值优先与Apollo
 */
public class SecretsManagerContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext>, EnvironmentPostProcessor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SecretsManagerContextInitializer.class);

    private CompositePropertySource secretsManagerPropertySource;

    // 比Apollo的order要低一级,让ApolloApplicationContextInitializer先加载，让accessKey和secretKey，region可以配置在Apollo里面
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        SecretsManagerService secretsManagerService = this.buildSecretsManager(environment);
        if (secretsManagerService != null) {
            // 注册SecretsManagerService给业务使用
            context.getBeanFactory().registerSingleton(SecretsManagerService.class.getSimpleName(),
                secretsManagerService);
            context.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {

                @Override
                public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                    if (secretsManagerPropertySource != null) {
                        environment.getPropertySources().addFirst(secretsManagerPropertySource);
                    }
                    Map<String, SecretManagerPullService> afterSecretManagerPullService =
                        beanFactory.getBeansOfType(SecretManagerPullService.class);
                    if (afterSecretManagerPullService != null && !afterSecretManagerPullService.isEmpty()) {
                        List<SecretManagerPullService> serviceList =
                            Lists.newArrayList(afterSecretManagerPullService.values());
                        Collections.sort(serviceList, new Comparator<SecretManagerPullService>() {

                            @Override
                            public int compare(SecretManagerPullService o1, SecretManagerPullService o2) {
                                return o1.getOrder() - o2.getOrder();
                            }

                        });
                        serviceList.forEach(service -> {
                            service.execut(secretsManagerPropertySource);
                        });
                    }
                }

            });
            // 加载Apollo的Listener，让Apollo的Listenner变更能够重新拉去secretmanager的值
            Config configApplication = ConfigService.getAppConfig();
            Config configInfraAll = ConfigService.getConfig(ApolloApplicationRunListener.CONFIGCENTER_INFRA_NAMESPACE);
            SecretsValueRefreshListener refetchListener =
                new SecretsValueRefreshListener(secretsManagerService, context);
            configApplication.addChangeListener(refetchListener);
            configInfraAll.addChangeListener(refetchListener);
        }
    }

    private SecretsManagerService buildSecretsManager(ConfigurableEnvironment environment) {
        SecretsManagerService secretsManagerService = new SecretsManagerService(environment);
        if (secretsManagerService.isExistSecretsManager()) {
            CompositePropertySource composite = secretsManagerService.fetchAll();
            if (composite != null) {
                this.secretsManagerPropertySource = composite;
                environment.getPropertySources().addFirst(composite);
            }
            return secretsManagerService;
        } else {
            return null;
        }
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        boolean apooloBootstrapHasLoaded =
            environment.getPropertySources().contains(APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME);
        if (apooloBootstrapHasLoaded) {
            boolean secretManagerHasLoaded =
                environment.getPropertySources().contains(SECRETSMANAGER_PROPERTY_SOURCE_NAME);
            if (secretManagerHasLoaded) {
                environment.getPropertySources().remove(SECRETSMANAGER_PROPERTY_SOURCE_NAME);
            }
            if (secretsManagerPropertySource != null) {
                environment.getPropertySources().addFirst(secretsManagerPropertySource);
            }
        }
    }

    // 借助Apollo重新placeHolder注入的值
    public static void rePlaceHolderAllField(String[] keys, ConfigurableListableBeanFactory beanFactory) {
        SpringValueRegistry springValueRegistry = SpringInjector.getInstance(SpringValueRegistry.class);
        PlaceholderHelper placeholderHelper = SpringInjector.getInstance(PlaceholderHelper.class);
        TypeConverter typeConverter = beanFactory.getTypeConverter();
        for (String key : keys) {
            Collection<SpringValue> targetValues = springValueRegistry.get(beanFactory, key);
            if (targetValues == null || targetValues.isEmpty()) {
                continue;
            }
            for (SpringValue springValue : targetValues) {
                try {
                    Object value = placeholderHelper.resolvePropertyValue(beanFactory, springValue.getBeanName(),
                        springValue.getPlaceholder());
                    value =
                        typeConverter.convertIfNecessary(value, springValue.getTargetType(), springValue.getField());
                    springValue.update(value);
                } catch (Throwable e) {
                    logger.error("Auto update changed value failed, {}", springValue.toString(), e);
                }
            }
        }
    }

    private class SecretsValueRefreshListener implements ConfigChangeListener {

        private final SecretsManagerService secretsManagerService;

        private final ConfigurableApplicationContext context;

        private final ConfigurableListableBeanFactory beanFactory;

        public SecretsValueRefreshListener(SecretsManagerService secretsManagerService,
            ConfigurableApplicationContext context) {
            this.secretsManagerService = secretsManagerService;
            this.context = context;
            this.beanFactory = context.getBeanFactory();
        }

        private CompositePropertySource resetSecretManagerPropertySource() {
            MutablePropertySources mutablePropertySource = context.getEnvironment().getPropertySources();
            CompositePropertySource composite = secretsManagerService.fetchAll();
            if (composite != null) {
                mutablePropertySource.remove(SECRETSMANAGER_PROPERTY_SOURCE_NAME);
                mutablePropertySource.addFirst(composite);
            }
            return composite;
        }

        @Override
        public void onChange(ConfigChangeEvent changeEvent) {
            for (String key : changeEvent.changedKeys()) {
                if (SecretsManagerConstants.AWS_SECRET_REFETCH.equals(key)) {
                    ConfigChange change = changeEvent.getChange(key);
                    if (change.getChangeType() == MODIFIED) {
                        if ("true".equalsIgnoreCase(change.getNewValue())) {
                            CompositePropertySource propertySource = resetSecretManagerPropertySource();
                            if (propertySource != null) {
                                rePlaceHolderAllField(propertySource.getPropertyNames(), beanFactory);
                            }
                        }
                    }
                }
            }
        }
    }

}
