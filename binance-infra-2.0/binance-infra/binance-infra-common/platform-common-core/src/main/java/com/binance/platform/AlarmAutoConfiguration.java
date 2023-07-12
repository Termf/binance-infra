package com.binance.platform;

import com.binance.platform.common.AlarmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author Li Fenggang Date: 2020/11/23 4:47 下午
 */
@Configuration
@ConditionalOnBean(type = {"org.springframework.cloud.client.discovery.DiscoveryClient"})
@AutoConfigureAfter(name = {"org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration",
    "org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration"})
public class AlarmAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AlarmAutoConfiguration.class);
    @Autowired(required = false)
    private DiscoveryClient discoveryClient;
    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        if (discoveryClient != null && applicationContext != null) {
            log.info("Start init AlarmUtil");
            List<HttpMessageConverter<?>> messageConverters = Arrays.asList(new MappingJackson2HttpMessageConverter());
            RestTemplate restTemplate = new RestTemplate(messageConverters);
            AlarmUtilInitial.init(applicationContext, discoveryClient, restTemplate);
        } else {
            log.info("dependencies not found, AlarmUtil not init");
        }
    }

    static class AlarmUtilInitial extends AlarmUtil {
        public static void init(ApplicationContext applicationContext, DiscoveryClient discoveryClient,
            RestTemplate restTemplate) {
            AlarmUtil.init(applicationContext, discoveryClient, restTemplate);
        }
    }
}
