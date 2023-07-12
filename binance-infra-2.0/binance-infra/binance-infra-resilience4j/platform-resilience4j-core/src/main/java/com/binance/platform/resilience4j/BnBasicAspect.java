package com.binance.platform.resilience4j;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.RES4J_USEFLAG;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.KEY_UFLAG;

/**
 * author: sait xuc
 */
public class BnBasicAspect {

    private static final Pattern PATTERN = Pattern.compile("\\Q${\\E(.+?)\\Q}\\E");

    private static final Map<String, Pair<String, String>> PROPERTY_KEYS = Maps.newHashMap();

    public boolean isUseValue(ApplicationContext applicationContext) {
        boolean usedValue = true;
        String pvalue = applicationContext.getEnvironment().getProperty(RES4J_USEFLAG);
        if(StringUtils.isNoneBlank(pvalue)
                && pvalue.equalsIgnoreCase(KEY_UFLAG)) {
            usedValue = false;
        }
        return usedValue;
    }

    public String getProperty(String text, ApplicationContext applicationContext) {
        String propertyValue = getEnvironmentProperty(text, applicationContext);
        return propertyValue;
    }

    private String getEnvironmentProperty(String text, ApplicationContext applicationContext) {
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
                }else{

                    String key = text.substring(1);

                    String value = applicationContext.getEnvironment().getProperty(key);
                    if (value != null) {
                        return value;
                    } else {
                        return key;
                    }
                }


            }

        }

    }

    protected Method getFallbackMethod(Object fallbackInstance, String fname, Method orgmethed) {
        Method fallbackMethod;
        try {
            fallbackMethod = fallbackInstance.getClass()
                    .getMethod(fname, orgmethed.getParameterTypes());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Cannot use the fallback ["
                    + fallbackInstance.getClass() + "] for ["
                    + orgmethed.getDeclaringClass() + "]", e);
        }
        return fallbackMethod;
    }

}
