package com.binance.platform.openfeign.gray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.openfeign.gray.feign.GrayPatternMatcherRequestInterceptor;
import com.binance.platform.openfeign.gray.feign.OpenFeignGrayHeaderCustomizer;
import com.binance.platform.openfeign.gray.httpclient.GrayHttpRequestInterceptor;
import com.binance.platform.openfeign.gray.httpclient.HttpClientBuilderCustomizer;
import com.binance.platform.openfeign.gray.httpclient.HttpClientGrayHeaderCustomizer;

@Configuration
public class GrayAutoConfiguration {

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    public GrayHttpRequestInterceptor
        grayHttpRequestInterceptor(HttpClientGrayHeaderCustomizer httpClientGrayHeaderCustomizer) {
        return new GrayHttpRequestInterceptor(httpClientGrayHeaderCustomizer);
    }

    @Bean
    public GrayPatternMatcherRequestInterceptor
        grayPatternMatcherRequestInterceptor(OpenFeignGrayHeaderCustomizer openFeignGrayHeaderCustomizer) {
        return new GrayPatternMatcherRequestInterceptor(openFeignGrayHeaderCustomizer);
    }

    @Bean
    public HttpClientBuilderCustomizer
        httpClientBuilderCustomizer(GrayHttpRequestInterceptor grayHttpRequestInterceptor) {
        return new HttpClientBuilderCustomizer(grayHttpRequestInterceptor);
    }

    @Bean
    public HttpClientGrayHeaderCustomizer httpClientGrayHeaderCustomizer() {
        return new HttpClientGrayHeaderCustomizer(context);
    }

    @Bean
    public OpenFeignGrayHeaderCustomizer openFeignGrayHeaderCustomizer() {
        return new OpenFeignGrayHeaderCustomizer(context);
    }

    // @Bean
    // public GrayClientHttpRequestInterceptor
    // grayClientHttpRequestInterceptor(RestTemplateGrayHeaderCustomizer restTemplateGrayHeaderCustomizer) {
    // return new GrayClientHttpRequestInterceptor(restTemplateGrayHeaderCustomizer);
    // }
    // @Bean
    // public RestTemplateGrayHeaderCustomizer restTemplateGrayHeaderCustomizer() {
    // return new RestTemplateGrayHeaderCustomizer();
    // }
    // @Bean
    // public RestTemplateInitializingBean restTemplateInitializingBean() {
    // return new RestTemplateInitializingBean();
    // }

}
