package com.binance.platform.monitor.configuration;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.binance.platform.monitor.metric.resttemplate.MetricsClientHttpRequestInterceptor;
import com.binance.platform.monitor.metric.resttemplate.RestTemplatePostProcessor;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
@Configuration
public class RestTemplateAutoConfiguration {

    @Configuration
    @ConditionalOnClass({ClientHttpRequestInterceptor.class, RestTemplate.class})
    public static class WebConfig {
        @Bean
        public SmartInitializingSingleton metricsAsyncRestTemplateInitializer(
            final ObjectProvider<List<AsyncRestTemplate>> asyncRestTemplatesProvider,
            final RestTemplatePostProcessor customizer) {
            return () -> {
                final List<AsyncRestTemplate> asyncRestTemplates = asyncRestTemplatesProvider.getIfAvailable();
                if (!CollectionUtils.isEmpty(asyncRestTemplates)) {
                    asyncRestTemplates.forEach(customizer::customize);
                }
            };
        }

        @Bean
        public MetricsClientHttpRequestInterceptor metricsClientHttpRequestInterceptor() {
            return new MetricsClientHttpRequestInterceptor(METRIC_NAME);
        }

        @Bean
        public SmartInitializingSingleton metricsRestTemplateInitializer(
            final ObjectProvider<List<RestTemplate>> restTemplatesProvider,
            final RestTemplatePostProcessor customizer) {
            return () -> {
                final List<RestTemplate> restTemplates = restTemplatesProvider.getIfAvailable();
                if (!CollectionUtils.isEmpty(restTemplates)) {
                    restTemplates.forEach(customizer::customize);
                }
            };
        }

        @Bean
        public RestTemplatePostProcessor
            restTemplatePostProcessor(MetricsClientHttpRequestInterceptor metricsClientHttpRequestInterceptor) {
            return new RestTemplatePostProcessor(metricsClientHttpRequestInterceptor);
        }
    }

    public static final String METRIC_NAME = "rest_template_timer";

}
