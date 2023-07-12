
package com.binance.platform;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.binance.platform.eureka.wrapper.EurekaServiceRegistryWrapper;
import com.binance.platform.ribbon.wrapper.LoadBalancedRetryPolicyFactoryWrapper;

public class EurekaRibbonApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof EurekaServiceRegistry) {
					EurekaServiceRegistry eurekaServiceRegistry = (EurekaServiceRegistry) bean;
					return new EurekaServiceRegistryWrapper(eurekaServiceRegistry, applicationContext);
				} else if (bean instanceof EurekaInstanceConfigBean) {
					EurekaInstanceConfigBean instanceConfig = (EurekaInstanceConfigBean) bean;
					instanceConfig.setPreferIpAddress(true);
					return bean;
				} 
				else if (bean instanceof LoadBalancedRetryFactory) {
					LoadBalancedRetryFactory loadBalancedRetryPolicy = (LoadBalancedRetryFactory) bean;
					LoadBalancedRetryPolicyFactoryWrapper wrapper = new LoadBalancedRetryPolicyFactoryWrapper(
							loadBalancedRetryPolicy);
					LoadBalancedRetryPolicyFactoryWrapper.setContext(applicationContext);
					return wrapper;
				} 
				else {
					return bean;
				}
			}
		});
	}

}
