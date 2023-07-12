package com.binance.platform.resilience4j.ratelimiter.server.redis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

public interface RedisLuaSupport {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object evalScript(ApplicationContext applicationContext, RedisScript script, List<String> key,
			List<String> param) {
		RedisTemplate redisTemplate = getRedisTemplate(applicationContext);
		if (redisTemplate != null) {
			Object result = redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
					Object nativeConnection = redisConnection.getNativeConnection();
					if (StringUtils.containsIgnoreCase(nativeConnection.getClass().getName(), "jedis")) {
						return new JedisLuaSupport(nativeConnection).evalScript(script.getScriptAsString(), key, param);
					} else if (StringUtils.containsIgnoreCase(nativeConnection.getClass().getName(), "lettuce")) {
						return new LettuceLuaSupport(nativeConnection).evalScript(script.getScriptAsString(), key,
								param);
					}
					return 0L;
				}

			});
			return result;
		}
		return 0L;

	}

	public static RedisTemplate getRedisTemplate(ApplicationContext applicationContext) {
		RedisTemplate redisTemplate = null;
		try {
			redisTemplate = applicationContext.getBean("RateLimiterRedisTemplate", RedisTemplate.class);
		} catch (Exception e) {
			redisTemplate = null;
		}
		try {
			if (redisTemplate == null) {
				Map<String, RedisTemplate> redisTemplatesMap = applicationContext.getBeansOfType(RedisTemplate.class);
				if (redisTemplatesMap != null && redisTemplatesMap.size() > 0) {
					redisTemplate = redisTemplatesMap.values().iterator().next();
				}
			}
		} catch (Throwable e) {
			redisTemplate = null;
		}
		return redisTemplate;
	}

	public Object evalScript(String script, List<String> key, List<String> param);
}
