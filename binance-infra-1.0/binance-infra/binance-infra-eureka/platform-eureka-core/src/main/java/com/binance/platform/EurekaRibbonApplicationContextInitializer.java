
package com.binance.platform;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.eureka.wrapper.EurekaServiceRegistryWrapper;
import com.binance.platform.ribbon.wrapper.LoadBalancedRetryPolicyFactoryWrapper;

public class EurekaRibbonApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof EurekaServiceRegistry) {
                    EurekaServiceRegistry eurekaServiceRegistry = (EurekaServiceRegistry)bean;
                    return new EurekaServiceRegistryWrapper(eurekaServiceRegistry, environment);
                } else if (bean instanceof EurekaInstanceConfigBean) {
                    EurekaInstanceConfigBean instanceConfig = (EurekaInstanceConfigBean)bean;
                    instanceConfig.setPreferIpAddress(true);
                    return bean;
                } else if (bean instanceof LoadBalancedRetryPolicyFactory) {
                    LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory =
                        (LoadBalancedRetryPolicyFactory)bean;
                    LoadBalancedRetryPolicyFactoryWrapper wrapper =
                        new LoadBalancedRetryPolicyFactoryWrapper(loadBalancedRetryPolicyFactory);
                    LoadBalancedRetryPolicyFactoryWrapper.setContext(applicationContext);
                    return wrapper;
                } else {
                    return bean;
                }
            }
        });
    }

}
