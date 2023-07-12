package com.binance.platform.openfeign.idempotent.store;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.binance.platform.openfeign.idempotent.IdempotentRepository;

public class RedisIdempotentRepository implements IdempotentRepository {
    private static final int DEFAULT_IDEMPOTENT_KEY_EXPIRETIME = 60;
    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> redisHash;

    private final int expireTime;

    public RedisIdempotentRepository(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.redisTemplate = redisTemplate;
        this.redisHash = this.redisTemplate.opsForHash();
        this.expireTime = DEFAULT_IDEMPOTENT_KEY_EXPIRETIME;

    }

    public RedisIdempotentRepository(RedisConnectionFactory redisConnectionFactory, int expireTime) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.redisTemplate = redisTemplate;
        this.redisHash = this.redisTemplate.opsForHash();
        this.expireTime = expireTime;

    }

    @Override
    public boolean register(String appName, String idempotencyKey, String request) {
        try {
            redisHash.put(appName, idempotencyKey, request);
            redisTemplate.expire(appName, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean unregister(String appName, String idempotencyKey) {
        try {
            redisHash.delete(appName, idempotencyKey);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean exist(String appName, String idempotencyKey) {
        try {
            return redisHash.hasKey(appName, idempotencyKey);
        } catch (Throwable e) {
            return false;
        }
    }

}
