package com.binance.platform.redis.lock.support;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.binance.platform.redis.lock.RedisMultipleLock;

public final class ClusterRedLockHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterRedLockHelper.class);

    private final RedisMultipleLock redLock;

    private final StringRedisTemplate redisTemplate;

    public ClusterRedLockHelper(final RedisMultipleLock redLock) {
        this.redLock = redLock;
        this.redisTemplate = redLock.getRedisTemplate();
    }

    public ClusterRedLockHelper(final RedisMultipleLock redLock, final StringRedisTemplate redisTemplate,
        final List<String> lockKeys) {
        this.redLock = redLock;
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(final String lockKey, final String thread, final long lockWaitTime, final long expireTime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                try {
                    return redLock.realLock(nativeConnection, lockKey, thread, lockWaitTime, expireTime);
                } catch (Throwable e) {
                    LOGGER.error(
                        String.format("Redis Node %s Lock key: %s failed", nativeConnection.toString(), lockKey), e);
                    return false;
                }
            }

        });

    }

    public boolean unlock(final String lockKey, final String thread) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                try {
                    return redLock.realUnLock(nativeConnection, lockKey, thread);
                } catch (Throwable e) {
                    LOGGER.error(
                        String.format("Redis Node %s unLock key: %s failed", nativeConnection.toString(), lockKey), e);
                    return false;
                }
            }

        });

    }
}
