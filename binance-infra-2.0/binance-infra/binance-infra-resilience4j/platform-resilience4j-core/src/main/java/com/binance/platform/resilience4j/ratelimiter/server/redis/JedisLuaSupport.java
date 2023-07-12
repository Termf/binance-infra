package com.binance.platform.resilience4j.ratelimiter.server.redis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class JedisLuaSupport implements RedisLuaSupport {

	private final JedisCluster jedisCluster;

	private final Jedis jedis;

	public JedisLuaSupport(Object nativeConnection) {
		if (nativeConnection instanceof JedisCluster) {
			this.jedisCluster = (JedisCluster) nativeConnection;
			this.jedis = null;
		}
		// 单机模式
		else if (nativeConnection instanceof Jedis) {
			this.jedis = (Jedis) nativeConnection;
			this.jedisCluster = null;
		} else {
			this.jedis = null;
			this.jedisCluster = null;
		}
	}

	@Override
	public Object evalScript(String script, List<String> key, List<String> param) {
		if (jedisCluster != null) {
			return jedisCluster.eval(script, key, param);
		} else if (jedis != null) {
			return jedis.eval(script, key, param);
		} else {
			return 0L;
		}
	}

}
