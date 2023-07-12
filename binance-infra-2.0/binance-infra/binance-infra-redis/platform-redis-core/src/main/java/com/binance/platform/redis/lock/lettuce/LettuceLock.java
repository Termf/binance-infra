package com.binance.platform.redis.lock.lettuce;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.binance.platform.redis.lock.RedisMultipleLock;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.async.RedisScriptingAsyncCommands;

public class LettuceLock extends RedisMultipleLock {

    public LettuceLock(RedisProperties redisPropties, StringRedisTemplate redisTemplate, Environment env) {
        super(redisPropties, redisTemplate, env);
    }

    @Override
    public boolean realLock(Object nativeConnection, String key, String thread, long waitTime, long expireTime)
        throws Throwable {
        RedisScriptingAsyncCommands lettuce = (RedisScriptingAsyncCommands)nativeConnection;
        long start = System.currentTimeMillis();
        long duration = 0;
        boolean success = false;
        String script = lockScript.getScriptAsString();
        Pair<List<String>, List<String>> luaParam = this.lockLuaParam(key, thread, expireTime);
        byte[][] k = new byte[][] {luaParam.getLeft().get(0).getBytes()};
        byte[] v1 = luaParam.getRight().get(0).getBytes();
        byte[] v2 = luaParam.getRight().get(1).getBytes();
        while (!success && (duration <= waitTime)) {
            Object result = lettuce.eval(script, ScriptOutputType.INTEGER, k, v1, v2).get();
            if (result != null) {
                Long intergerResult = (Long)result;
                if (intergerResult > 0) {
                    success = true;
                    return success;
                } else {
                    // 睡眠一段时间再重试
                    try {
                        Thread.sleep(10, (int)(Math.random() * 500));
                    } catch (InterruptedException e) {
                    }
                }
                duration = System.currentTimeMillis() - start;
            }
        }
        return success;
    }

    @Override
    public boolean realUnLock(Object nativeConnection, String key, String thread) throws Throwable {
        RedisScriptingAsyncCommands lettuce = (RedisScriptingAsyncCommands)nativeConnection;
        String script = unlockScript.getScriptAsString();
        Pair<List<String>, List<String>> luaParam = this.unLockLuaParam(key, thread);
        byte[][] k = new byte[][] {luaParam.getLeft().get(0).getBytes()};
        byte[] v = luaParam.getRight().get(0).getBytes();
        Object result = lettuce.eval(script, ScriptOutputType.INTEGER, k, v).get();
        if (result != null && (Long)result == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
