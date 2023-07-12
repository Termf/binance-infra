package com.binance.platform.redis.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import com.binance.platform.redis.RedisLock;
import com.binance.platform.redis.lock.support.RedisLockThreadContext;

public abstract class RedisSingleLock implements RedisLock {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RedisSingleLock.class);

    private final Environment env;

    protected DefaultRedisScript<Long> lockScript;

    protected DefaultRedisScript<Long> unlockScript;

    protected final StringRedisTemplate redisTemplate;

    public RedisSingleLock(StringRedisTemplate redisTemplate, Environment env) {
        this.initLuaScript();
        this.env = env;
        this.redisTemplate = redisTemplate;
    }

    private void initLuaScript() {
        // lock script
        lockScript = new DefaultRedisScript<Long>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/lock.lua")));
        lockScript.setResultType(Long.class);
        LOGGER.debug("init lock lua script success:{}", lockScript.getScriptAsString());
        // unlock script
        unlockScript = new DefaultRedisScript<Long>();
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock/unlock.lua")));
        unlockScript.setResultType(Long.class);
        LOGGER.debug("init release lua script success:{}", unlockScript.getScriptAsString());
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
    public void lock() {
        tryLock();
    }

    @Override
    public boolean tryLock() {
        return tryLock(getDefaultKey(), DEFAULT_WAIT_MILLS, DEFAULT_EXPIRE_MILLS);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        Long millis = unit.toMillis(time);
        return tryLock(getDefaultKey(), millis, DEFAULT_EXPIRE_MILLS);
    }

    @Override
    public void unlock() {
        unlock(getDefaultKey());
    }

    @Override
    public boolean tryLock(String key, long wait, long expire) {
        return lockInternal(key, wait, expire);
    }

    @Override
    public void unlock(String key) {
        unlockInternal(key);
    }

    public String getDefaultKey() {
        return env.getProperty("spring.application.name") + GLOBAL_LOCK_DEFAULT_KEY;
    }

    public Integer getDefaultLocks() {
        String locks = env.getProperty("spring.redis.locks");
        return locks != null ? Integer.valueOf(locks) : DEFAULT_LOCKS;
    }

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    protected boolean lockInternal(String key, long waitTime, long expireTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                try {
                    String threadId = RedisLockThreadContext.getContext().getCurrentThreadId();
                    return realLock(nativeConnection, key, threadId, waitTime, expireTime);
                } catch (Throwable e) {
                    LOGGER.error(String.format("Redis Node %s Lock key: %s failed", nativeConnection.toString(), key),
                        e);
                    return false;
                }
            }

        });

    }

    protected boolean unlockInternal(String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return redisTemplate.execute(new RedisCallback<Boolean>() {

                    @Override
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        String threadId = RedisLockThreadContext.getContext().removeCurrentThreadId();
                        if (threadId != null) {
                            Object nativeConnection = connection.getNativeConnection();
                            try {
                                return realUnLock(nativeConnection, key, threadId);
                            } catch (Throwable e) {
                                LOGGER.error(String.format("Redis Node %s unLock key: %s failed",
                                    nativeConnection.toString(), key), e);
                                return false;
                            }
                        } else {
                            return true;
                        }
                    }

                });
            }

        });

    }

    public Pair<List<String>, List<String>> lockLuaParam(String key, String thread, long expireTime) {
        // lua脚本的key值
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        // lua脚本的argv值
        List<String> args = new ArrayList<String>();
        args.add(thread);
        args.add(String.valueOf(expireTime));
        return new ImmutablePair<List<String>, List<String>>(keys, args);
    }

    public Pair<List<String>, List<String>> unLockLuaParam(String key, String thread) {
        // lua脚本的key值
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        // lua脚本的argv值
        List<String> args = new ArrayList<String>();
        args.add(thread);
        return new ImmutablePair<List<String>, List<String>>(keys, args);
    }

    public abstract boolean realLock(Object nativeConnection, String key, String thread, long waitTime, long expireTime)
        throws Throwable;

    public abstract boolean realUnLock(Object nativeConnection, String key, String thread) throws Throwable;

}
