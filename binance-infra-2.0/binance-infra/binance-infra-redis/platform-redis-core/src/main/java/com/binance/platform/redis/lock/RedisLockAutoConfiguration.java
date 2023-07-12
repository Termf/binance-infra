package com.binance.platform.redis.lock;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisUseJedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.binance.master.utils.RedisCacheUtils;
import com.binance.platform.redis.RedisLock;
import com.binance.platform.redis.lock.jedis.ConsistentHashJedisLock;
import com.binance.platform.redis.lock.jedis.JedisLock;
import com.binance.platform.redis.lock.lettuce.LettuceLock;

@Configuration
@Conditional(RedisUseJedisAutoConfiguration.RedisCondition.class)
@AutoConfigureAfter(RedisUseJedisAutoConfiguration.class)
@AutoConfigureBefore(CacheAutoConfiguration.class)
public class RedisLockAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "io.lettuce.core.RedisClient")
    @ConditionalOnBean(type = {"org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory"})
    public RedisLock luttuceRedisLock(RedisProperties redisProperties, StringRedisTemplate stringRedisTempalte,
        Environment env) {
        RedisLock redisLock = new LettuceLock(redisProperties, stringRedisTempalte, env);
        RedisCacheUtils.setRedisLock(redisLock);
        return redisLock;
    }

    @Bean
    @Primary
    @ConditionalOnClass(name = {"org.springframework.data.redis.connection.jedis.JedisConnection",
        "redis.clients.jedis.Jedis", "org.apache.commons.pool2.impl.GenericObjectPool"})
    @ConditionalOnBean(type = {"org.springframework.data.redis.connection.jedis.JedisConnectionFactory"})
    public RedisLock jedisRedisLock(RedisProperties redisProperties, StringRedisTemplate stringRedisTempalte,
        Environment env) {
        RedisLock redisLock = new JedisLock(redisProperties, stringRedisTempalte, env);
        RedisCacheUtils.setRedisLock(redisLock);
        return redisLock;
    }

    @Bean(name = "consistentHashJedisLock")
    @ConditionalOnClass(name = {"org.springframework.data.redis.connection.jedis.JedisConnection",
        "redis.clients.jedis.Jedis", "org.apache.commons.pool2.impl.GenericObjectPool"})
    @ConditionalOnBean(type = {"org.springframework.data.redis.connection.jedis.JedisConnectionFactory"})
    public ConsistentHashJedisLock consistentHashJedisLock(StringRedisTemplate stringRedisTempalte) {
        ConsistentHashJedisLock redisLock = new ConsistentHashJedisLock(stringRedisTempalte);
        return redisLock;
    }

}
