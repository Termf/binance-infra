package com.binance.platform.monitor.configuration;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrcisEnhance;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;

public class MonitorApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof JvmGcMetrics) {
                    JvmGcMetrics jvmGcMetrics = (JvmGcMetrics)bean;
                    Field field = ReflectionUtils.findField(JvmGcMetrics.class, "tags");
                    ReflectionUtils.makeAccessible(field);
                    Iterable<Tag> tags = (Iterable<Tag>)ReflectionUtils.getField(field, jvmGcMetrics);
                    return tags == null ? new JvmGcMetrcisEnhance() : new JvmGcMetrcisEnhance(tags);
                }
                return bean;

            }
        });
    }

}
