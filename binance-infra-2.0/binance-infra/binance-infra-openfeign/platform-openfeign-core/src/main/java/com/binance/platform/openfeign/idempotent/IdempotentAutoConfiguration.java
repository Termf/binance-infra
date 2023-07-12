package com.binance.platform.openfeign.idempotent;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.binance.platform.openfeign.idempotent.store.RedisIdempotentRepository;

@Configuration
public class IdempotentAutoConfiguration {

	static class FeignClientCondition extends SpringBootCondition {
		@Override
		public ConditionOutcome getMatchOutcome(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
			try {
				Map<String, Object> feignClients = context.getBeanFactory()
						.getBeansWithAnnotation(EnableFeignClients.class);
				return new ConditionOutcome(feignClients != null && !feignClients.isEmpty(),
						"spring.cloud.feignclients");
			} catch (Exception e) {
				return new ConditionOutcome(false, "spring.cloud.feignclients");
			}

		}
	}

	/**
	 * 只有配置了FeignClient扫描并且开启了开关时才加载
	 */
	@Configuration
	@Conditional(FeignClientCondition.class)
	@ConditionalOnProperty(name = "com.binance.intra.idempotence.switch", havingValue = "true", matchIfMissing = false)
	protected class ConsumerSupportConfig {

		@Bean
		public IdempotentInterceptor idempotentInterceptor(Environment env) {
			return new IdempotentInterceptor(env);
		}

	}

	@Configuration
	@ConditionalOnProperty(name = "com.binance.intra.idempotence.switch", havingValue = "true", matchIfMissing = false)
	@ConditionalOnWebApplication
	protected class ProviderSupportConfig implements WebMvcConfigurer {

		@Value("${server.error.path:${error.path:/error}}")
		private String errorPath;

		@Autowired
		private Environment env;

		@Autowired
		private ConfigurableApplicationContext context;

		@Autowired(required = false)
		private IdempotentRepository idempotentRepository;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			if (idempotentRepository != null) {
				registry.addInterceptor(new IdempotentInterceptor(env, idempotentRepository))//
						.addPathPatterns("/**").excludePathPatterns("/**" + errorPath);
			} else {
				if (isPresent("org.springframework.data.redis.connection.RedisConnectionFactory")) {
					try {
						RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
						registry.addInterceptor(
								new IdempotentInterceptor(env, new RedisIdempotentRepository(redisConnectionFactory)))//
								.addPathPatterns("/**").excludePathPatterns("/**" + errorPath);
					} catch (BeansException e) {
						// igore
					}
				}
			}
		}

	}

	private static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

}
