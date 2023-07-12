package com.binance.platform.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.common.ExcludeAutoConfigurationUtil;

public class RedisEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String[] EXCLUDEAUTOCONFIGALL =
        new String[] {"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"};

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ExcludeAutoConfigurationUtil.excludeAutoConfiguration(environment, EXCLUDEAUTOCONFIGALL);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
