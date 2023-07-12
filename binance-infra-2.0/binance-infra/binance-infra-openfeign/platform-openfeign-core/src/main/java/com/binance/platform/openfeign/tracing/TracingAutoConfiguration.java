package com.binance.platform.openfeign.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TracingAutoConfiguration {

	@Bean
	public TracingInterceptor tracingInterceptor(Environment env) {
		String serviceName = env.getProperty("spring.application.name", "None");
		return new TracingInterceptor(serviceName);
	}

}
