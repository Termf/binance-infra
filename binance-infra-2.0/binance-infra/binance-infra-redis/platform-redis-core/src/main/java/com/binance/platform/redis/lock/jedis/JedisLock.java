package com.binance.platform.redis.lock.jedis;

import com.binance.platform.redis.lock.RedisMultipleLock;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.List;

public class JedisLock extends RedisMultipleLock {

    public JedisLock(RedisProperties redisPropties, StringRedisTemplate redisTemplate, Environment env) {
        super(redisPropties, redisTemplate, env);
    }

    private Object eval(Object nativeConnection, String script, List<String> key, List<String> param) {
        if (nativeConnection instanceof JedisCluster) {
            JedisCluster jedis = (JedisCluster) nativeConnection;
            return jedis.eval(script, key, param);
        } else {
            Jedis jedis = (Jedis) nativeConnection;
            return jedis.eval(script, key, param);

        }

    }

    @Override
    public boolean realLock(Object nativeConnection, String key, String thread, long waitTime, long expireTime)
            throws Throwable {
        long start = System.currentTimeMillis();
        long duration = 0;
        boolean success = false;
        Pair<List<String>, List<String>> luaParam = this.lockLuaParam(key, thread, expireTime);
        while (!success && (duration <= waitTime)) {
            Object result =
                    eval(nativeConnection, lockScript.getScriptAsString(), luaParam.getLeft(), luaParam.getRight());
            if (result != null) {
                Long intergerResult = (Long) result;
                if (intergerResult > 0) {
                    success = true;
                    return success;
                } else {
                    // 睡眠一段时间再重试
                    try {
                        Thread.sleep(10, (int) (Math.random() * 500));
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
        Pair<List<String>, List<String>> luaParam = this.unLockLuaParam(key, thread);
        Object result =
                eval(nativeConnection, unlockScript.getScriptAsString(), luaParam.getLeft(), luaParam.getRight());
        if (result != null && (Long) result == 1) {
            return true;
        } else {
            return false;
        }
    }

}
