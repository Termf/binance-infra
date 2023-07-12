package com.binance.platform.common;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ReflectionUtils;

/**
 * 代码方式将Spring Boot的AutoConfiguration给exclude掉，不加载默认的AutoConfiguration
 * 
 * <pre>
 * 使用方法：
 *  1: 定义自己的EnvironmentPostProcessor
 *  2: 在postProcessEnvironment中，调用该静态方法进行exclude
 * </pre>
 */
public class ExcludeAutoConfigurationUtil {

    private static final String EXLUCDE_PROPERTY_SOURCENAME = "ExcludeAutoConfigurationPropertySources";

    public static final void excludeAutoConfiguration(ConfigurableEnvironment environment, String[] excludeConfig) {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        PropertySource excludeSource = findExcludePropertySource(mutablePropertySources.iterator());
        if (excludeSource != null) {
            int lastIndex = 0;
            while (excludeSource.containsProperty("spring.autoconfigure.exclude[" + lastIndex + "]")) {
                lastIndex++;
            }
            Object source = excludeSource.getSource();
            for (int i = 0; i < excludeConfig.length; i++) {
                lastIndex = lastIndex + i;
                if (source instanceof Map) {
                    Map mapSource = (Map)source;
                    if (!mapSource.containsValue(excludeConfig[i])) {
                        mapSource.put("spring.autoconfigure.exclude[" + lastIndex + "]", excludeConfig[i]);
                    }
                }
            }

        } else {
            Properties proerties = new Properties();
            for (int i = 0; i < excludeConfig.length; i++) {
                proerties.put("spring.autoconfigure.exclude[" + i + "]", excludeConfig[i]);
            }
            mutablePropertySources.addLast(new PropertiesPropertySource(EXLUCDE_PROPERTY_SOURCENAME, proerties));
        }

    }

    private static PropertySource findExcludePropertySource(Iterator<PropertySource<?>> iterator) {
        MapPropertySource returnPropertySource = null;
        while (iterator.hasNext()) {
            PropertySource propertySource = iterator.next();
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource enumerablePropertySource = (MapPropertySource)propertySource;
                String[] propertyNames = enumerablePropertySource.getPropertyNames();
                for (String propertyName : propertyNames) {
                    if (propertyName.startsWith("spring.autoconfigure.exclude")) {
                        returnPropertySource = enumerablePropertySource;
                        break;
                    }
                }
            }
            if (returnPropertySource != null) {
                break;
            }
        }
        if (returnPropertySource != null) {
            Method method = ReflectionUtils.findMethod(returnPropertySource.getClass(), "getPropertySources");
            if (method != null) {
                ReflectionUtils.makeAccessible(method);
                Object obj = ReflectionUtils.invokeMethod(method, returnPropertySource);
                if (obj instanceof CompositePropertySource) {
                    CompositePropertySource compositePropertySource = (CompositePropertySource)obj;
                    Collection<PropertySource<?>> collectionPropertySources =
                        compositePropertySource.getPropertySources();
                    return findExcludePropertySource(collectionPropertySources.iterator());
                }
            }
        }
        return returnPropertySource;

    }
}
