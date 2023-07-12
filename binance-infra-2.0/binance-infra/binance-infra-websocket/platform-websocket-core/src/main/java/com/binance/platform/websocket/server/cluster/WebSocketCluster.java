package com.binance.platform.websocket.server.cluster;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.binance.platform.websocket.SpringContextHolder;
import com.vip.vjtools.vjkit.net.NetUtil;

@SuppressWarnings(value = { "rawtypes" })
public class WebSocketCluster {

	protected final String LOCAL_WEBSOCKET_ADDRESS = NetUtil.getLocalHost() + ":"
			+ SpringContextHolder.getProperty("websocket.server.port");

	public RedisTemplate getRedisTemplate() {

		RedisTemplate redisTemplate = null;
		try {
			redisTemplate = SpringContextHolder.getBean("webSocketRedisTemplate", RedisTemplate.class);
		} catch (Throwable e) {
			redisTemplate = null;
		}
		try {
			if (redisTemplate == null) {
				List<RedisTemplate> redisTemplateList = SpringContextHolder.getBeans(RedisTemplate.class);
				if (redisTemplateList != null && redisTemplateList.size() > 0) {
					redisTemplate = redisTemplateList.get(0);
				}
			}
		} catch (Throwable e) {
			redisTemplate = null;
		}
		return redisTemplate;
	}
}
