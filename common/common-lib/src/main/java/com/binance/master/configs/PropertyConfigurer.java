package com.binance.master.configs;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
    private static ImmutableMap<String, String> ctxPropertiesMap;

    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_STAGE = "stage";
    public static final String ENV_PROD = "production";

    static {
        try {
            String confPath = System.getProperty("confPath");
            if (!StringUtils.isEmpty(confPath)) {
                FileInputStream fis = new FileInputStream(new File(confPath + "/env.properties"));
                Properties prop = new Properties();
                prop.load(fis);

                String env = prop.getProperty("env");

                if (ENV_DEV.equals(env) || ENV_TEST.equals(env) || ENV_PROD.equals(env) || ENV_STAGE.equals(env)) {
                    System.setProperty("env", env);
                } else {
                    throw new RuntimeException(".env file error:" + env);
                }

                fis.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException {

        super.processProperties(beanFactory, props);
        Map<String, String> tempContainer = Maps.newHashMap();
        if (ctxPropertiesMap != null) {
            ImmutableSet<Entry<String, String>> entrySet = ctxPropertiesMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                tempContainer.put(entry.getKey(), entry.getValue());
            }
        }
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            tempContainer.put(keyStr, value);
        }

        ctxPropertiesMap = ImmutableMap.copyOf(tempContainer);
    }

    // static method for accessing context properties
    public static String getContextProperty(String name) {
        return ctxPropertiesMap.get(name);
    }

    public static String getEnv() {
        return System.getProperty("env");
    }
}
