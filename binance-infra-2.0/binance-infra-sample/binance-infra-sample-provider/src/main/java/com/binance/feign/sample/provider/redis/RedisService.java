package com.binance.feign.sample.provider.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setKey(String key, String value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, value);

    }

}
