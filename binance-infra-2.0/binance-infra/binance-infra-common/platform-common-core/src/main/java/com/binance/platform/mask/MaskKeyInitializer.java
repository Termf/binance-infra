package com.binance.platform.mask;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.master.log.layout.MaskUtil;
import com.binance.master.utils.LogMaskUtils;
import com.binance.master.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author james.li
 */
@Order(value = Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class MaskKeyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {



    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        // initialize the customized mask keys for application
        String plainTextPatternsStr = env.getProperty("mask.key.plainTextPatterns");
        if (StringUtils.isNotEmpty(plainTextPatternsStr)) {
            List<String> plainTextPatterns = Arrays.asList(plainTextPatternsStr.split("\n"));
            MaskUtil.init(plainTextPatterns, env.getProperty("spring.application.name"));
            log.info("initialized the plain text patterns for MaskUtil");
        }

        String jsonFieldKeysStr = env.getProperty("mask.key.jsonFieldKeys");
        if (StringUtils.isNotEmpty(jsonFieldKeysStr)) {
            List<String> jasonFieldKeys = Arrays.asList(jsonFieldKeysStr.split(","));
            LogMaskUtils.init(jasonFieldKeys);
            log.info("initialized the json field keys for LogMaskUtils. jsonFieldKeys: {}", jsonFieldKeysStr);
        }

    }
}
