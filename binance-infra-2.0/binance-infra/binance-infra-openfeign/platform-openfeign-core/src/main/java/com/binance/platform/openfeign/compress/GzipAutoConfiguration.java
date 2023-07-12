package com.binance.platform.openfeign.compress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Deprecated
public class GzipAutoConfiguration implements WebMvcConfigurer {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GzipHandlerInterceptor())//
            .addPathPatterns("/**").excludePathPatterns("/**" + errorPath);
    }

 
    @Bean
    public GzipProperties gzipProperties() {
        return new GzipProperties();
    }
}
