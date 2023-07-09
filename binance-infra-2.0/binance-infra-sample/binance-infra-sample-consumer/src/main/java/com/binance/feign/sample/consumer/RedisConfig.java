package com.binance.feign.sample.consumer;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisUseJedisAutoConfiguration.CustomizeGenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "webSocketRedisTemplate")
    public RedisTemplate<String, Object>
        redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisSerializer<Object> serializer = new CustomizeGenericFastJsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
