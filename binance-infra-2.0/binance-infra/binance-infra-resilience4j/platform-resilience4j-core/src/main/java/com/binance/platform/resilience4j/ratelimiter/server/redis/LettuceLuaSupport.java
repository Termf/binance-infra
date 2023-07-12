package com.binance.platform.resilience4j.ratelimiter.server.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisScriptingAsyncCommands;

public class LettuceLuaSupport implements RedisLuaSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(LettuceLuaSupport.class);

	private final RedisScriptingAsyncCommands redisScriptingAsyncCommands;

	public LettuceLuaSupport(Object nativeConnection) {
		if (nativeConnection instanceof RedisScriptingAsyncCommands) {
			this.redisScriptingAsyncCommands = (RedisScriptingAsyncCommands) nativeConnection;
		} else {
			this.redisScriptingAsyncCommands = null;
		}

	}

	@Override
	public Object evalScript(String script, List<String> key, List<String> param) {
		if (this.redisScriptingAsyncCommands != null) {
			try {
				byte[][] k = new byte[][] { key.get(0).getBytes() };
				byte[] v1 = param.get(0).getBytes();
				byte[] v2 = param.get(1).getBytes();
				Object result = redisScriptingAsyncCommands.eval(script, ScriptOutputType.INTEGER, k, v1, v2).get();
				return result;
			} catch (Throwable e) {
				LOGGER.warn(e.getMessage(), e);
				return 0L;
			}
		}
		return 0L;
	}

}
