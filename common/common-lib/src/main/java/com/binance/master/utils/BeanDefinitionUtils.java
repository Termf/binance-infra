package com.binance.master.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;

public class BeanDefinitionUtils {

    /**
     * 构建对象的bean定义
     * 
     * @param obj
     * @return
     */
    public static <T> BeanDefinition buildBeanDefinition(T obj) {
        Class<? extends Object> clz = obj.getClass();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        ReflectionUtils.doWithLocalFields(clz, field -> {
            try {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    return;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                builder.addPropertyValue(field.getName(), field.get(obj));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return builder.getBeanDefinition();
    }

}
