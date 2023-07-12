package com.binance.platform.redis.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.support.NoOpCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.redis.cache.jetcache.JetCacheManager;
import com.binance.platform.redis.cache.support.JacksonKeyConverter;
import com.google.common.collect.Lists;

@Configuration
public class LocalCacheAutoConfiguration {

	@Configuration
	@EnableCaching
	@AutoConfigureAfter(JetCacheAutoConfiguration.class)
	public static class LocalCacheConfig extends CachingConfigurerSupport {

		private static final Logger logger = LoggerFactory.getLogger(LocalCacheConfig.class);

		@Value("${jetcache.enable:false}")
		private Boolean jetCacheEnable;

		private static JacksonKeyConverter jacksonKeyConverter = JacksonKeyConverter.INSTANCE;

		@Bean
		@Override
		public KeyGenerator keyGenerator() {
			return (target, method, params) -> {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(".");
				sb.append(method.getName());
				for (Object obj : params) {
					if (obj != null) {
						sb.append(":");
						sb.append(jacksonKeyConverter.apply(obj));
					}
				}
				return sb.toString();
			};
		}

		@Autowired(required = false)
		private CacheManager cacheManager;

		@Autowired(required = false)
		private JetCacheManager jetCacheManager;

		@PostConstruct
		public void init() {
			// 这样做的目的是允许应用上层定义自己的CacheManager，可以有不同的ttl配置，如果应用上层没定义，将会新建一个ConcurrentMapCacheManager作为默认实现
            if (jetCacheEnable && jetCacheManager != null) {
				this.cacheManager = jetCacheManager;
			} else {
				if (cacheManager == null) {
					this.cacheManager = new ConcurrentMapCacheManager("localCache");
				}
			}
		}

		@Bean
		@Override
		public CacheManager cacheManager() {
			return cacheManager;
		}

		@Override
		@Bean
		public CacheResolver cacheResolver() {
			return new SimpleCacheResolver(cacheManager) {
				@Override
				public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
					Collection<String> cacheNames = getCacheNames(context);
					if (cacheNames == null) {
						return Collections.emptyList();
					}
					Collection<Cache> result = new ArrayList<>(cacheNames.size());
					for (String cacheName : cacheNames) {
						Cache cache = null;
						try {
							cache = getCacheManager().getCache(cacheName);
						} catch (Exception e) {
							// 确保报错时不使用缓存，每次创建一个Cache即可。
							return Lists.newArrayList(new NoOpCache(cacheName));
						}
						if (cache == null) {
							if (cacheManager instanceof ConcurrentMapCacheManager) {
								// 这样创建的cache将会被cacheManager托管，避免失效问题
								((ConcurrentMapCacheManager) cacheManager).setCacheNames(Lists.newArrayList(cacheName));
								cache = cacheManager.getCache(cacheName);
							} else {
								throw new IllegalArgumentException(
										"Cannot find cache named '" + cacheName + "' for " + context.getOperation());
							}
						}
						result.add(cache);
					}
					return result;
				}
			};
		}

		@Bean
		@Override
		public CacheErrorHandler errorHandler() {
			CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
				@Override
				public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
					logger.error("Handle spring cache has get error：key=[{}]", key, exception);
				}

				@Override
				public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
					logger.error("Handle spring cache has put error：key=[{}]", key, exception);
				}

				@Override
				public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
					logger.error("Handle spring cache has evict error：key=[{}]", key, exception);
				}

				@Override
				public void handleCacheClearError(RuntimeException exception, Cache cache) {
					logger.error("Handle spring cache has clear error：key=[{}]", exception);
				}
			};
			return cacheErrorHandler;
		}

	}
}
