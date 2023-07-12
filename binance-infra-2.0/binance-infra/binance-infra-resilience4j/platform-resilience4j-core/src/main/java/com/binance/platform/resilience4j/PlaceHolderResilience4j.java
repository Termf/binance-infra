package com.binance.platform.resilience4j;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Maps;

public abstract class PlaceHolderResilience4j {

	private static final Map<String, Pair<String, String>> PROPERTY_KEYS = Maps.newHashMap();

	private static final Pattern PATTERN = Pattern.compile("\\Q${\\E(.+?)\\Q}\\E");

	protected ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public int getProperty(String text) {
		String propertyValue = getEnvironmentProperty(text);
		return Integer.valueOf(propertyValue).intValue();
	}

	private String getEnvironmentProperty(String text) {
		if (!text.startsWith("$")) {
			return text;
		} else {
			String propertyKey = PROPERTY_KEYS.get(text) != null ? PROPERTY_KEYS.get(text).getKey() : null;
			if (propertyKey != null) {
				String value = applicationContext.getEnvironment().getProperty(propertyKey);
				if (value != null) {
					return value;
				} else {
					return PROPERTY_KEYS.get(text).getValue() != null ? PROPERTY_KEYS.get(text).getValue() : text;
				}
			} else {
				Matcher matcher = PATTERN.matcher(text);
				if (matcher.find()) {
					String[] keyAndValue = StringUtils.split(matcher.group(1), ":");
					String key = keyAndValue[0];
					String defaultValue = null;
					if (keyAndValue.length > 1) {
						defaultValue = keyAndValue[1];
					}
					PROPERTY_KEYS.put(text, new ImmutablePair(key, defaultValue));
					String value = applicationContext.getEnvironment().getProperty(key);
					if (value != null) {
						return value;
					} else {
						return defaultValue != null ? defaultValue : text;
					}
				}
				return text;
			}

		}

	}
}
