package com.binance.platform.openfeign;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class OpenFeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final String FEIGN_CONTEXT_BEAN_NAME = "feignContext";
    private static final String EUREKA_AUTO_SERVICE_REGISTRATION_BEAN_NAME = "eurekaAutoServiceRegistration";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (containsBeanDefinition(beanFactory, FEIGN_CONTEXT_BEAN_NAME, EUREKA_AUTO_SERVICE_REGISTRATION_BEAN_NAME)) {
            BeanDefinition bd = beanFactory.getBeanDefinition(FEIGN_CONTEXT_BEAN_NAME);
            bd.setDependsOn(EUREKA_AUTO_SERVICE_REGISTRATION_BEAN_NAME);
        }
    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
    }

}
