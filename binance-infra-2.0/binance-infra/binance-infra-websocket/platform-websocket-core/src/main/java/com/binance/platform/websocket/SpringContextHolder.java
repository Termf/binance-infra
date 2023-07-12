package com.binance.platform.websocket;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class SpringContextHolder
		implements DisposableBean, ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);
	private static ApplicationContext applicationContext = null;
	private static Environment environment;

	public static void clearHolder() {
		applicationContext = null;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(Class<T> requiredType) {
		try {
			return applicationContext.getBean(requiredType);
		} catch (NoSuchBeanDefinitionException exception) {
			LOGGER.debug("not found bean in spring cotext", exception);
			return null;
		}

	}

	public static <T> T getBean(String beanName, Class<T> requiredType) {
		try {
			return applicationContext.getBean(beanName, requiredType);
		} catch (NoSuchBeanDefinitionException exception) {
			LOGGER.debug("not found bean in spring cotext", exception);
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getBeans(Class<T> requiredType) {
		try {
			Map<String, T> mapBeans = applicationContext.getBeansOfType(requiredType);
			if (mapBeans != null && mapBeans.size() != 0) {
				return mapBeans.values().stream().collect(Collectors.toList());
			}
			return Collections.EMPTY_LIST;
		} catch (NoSuchBeanDefinitionException exception) {
			return Collections.EMPTY_LIST;
		}

	}

	public static String getProperty(String property) {
		return getProperty(property, "");
	}

	public static String getProperty(String property, String defaultValue) {
		return environment.containsProperty(property) ? environment.getProperty(property) : defaultValue;
	}

	public static <T> T getProperty(String property, Class<T> clazz) {
		return environment.getProperty(property, clazz);
	}

	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
		SpringContextHolder.environment = applicationContext.getEnvironment();
	}
}
