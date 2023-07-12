package com.binance.platform.redis.lock.jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import com.binance.platform.redis.RedisLock;
import com.binance.platform.redis.lock.support.RedisLockThreadContext;
import com.google.common.collect.Lists;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class ConsistentHashJedisLock implements RedisLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsistentHashJedisLock.class);

    private final StringRedisTemplate redisTemplate;

    private String lockScript;

    private String unlockScript;

    public ConsistentHashJedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        // lock script
        DefaultRedisScript<Long> lockScript = new DefaultRedisScript<Long>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/lock.lua")));
        lockScript.setResultType(Long.class);
        this.lockScript = lockScript.getScriptAsString();
        LOGGER.debug("init lock lua script success:{}", lockScript.getScriptAsString());
        // unlock script
        DefaultRedisScript<Long> unlockScript = new DefaultRedisScript<Long>();
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/unlock.lua")));
        unlockScript.setResultType(Long.class);
        this.unlockScript = unlockScript.getScriptAsString();
        LOGGER.debug("init release lua script success:{}", unlockScript.getScriptAsString());
    }

    private Object eval(String script, List<String> key, List<String> param) {
        return redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) {
                    return ((JedisCluster)nativeConnection).eval(script, key, param);
                } else {
                    return ((Jedis)nativeConnection).eval(script, key, param);
                }
            }

        });

    }

    @Override
    public void lock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(String key, long wait, long expire) {
        long start = System.currentTimeMillis();
        long duration = 0;
        boolean success = false;
        String threadId = RedisLockThreadContext.getContext().getCurrentThreadId();
        while (!success && (duration <= wait)) {
            Object result =
                this.eval(lockScript, Lists.newArrayList(key), Lists.newArrayList(threadId, String.valueOf(expire)));
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
    public void unlock(String key) {
        String threadId = RedisLockThreadContext.getContext().removeCurrentThreadId();
        if (threadId == null) {
            return;
        } else {
            this.eval(unlockScript, Lists.newArrayList(key), Lists.newArrayList(threadId));
        }
    }

}
