package com.binance.platform.monitor.configuration;

import java.util.HashSet;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.redis.JedisPoolMeterBinder;
import com.binance.platform.monitor.metric.redis.LettucePoolMeterBinder;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

@AutoConfigureAfter(value = {org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
    MicrometerAutoConfiguration.class})
@ConditionalOnClass(RedisTemplate.class)
@Configuration
public class RedisAutoConfiguration {

    @Autowired
    private CustomizerTag platformTag;

    @Autowired(required = false)
    private Map<String, RedisConnectionFactory> redisConnectionFactoryMap;

    @PostConstruct
    public void init() {
        if (redisConnectionFactoryMap != null) {
            redisConnectionFactoryMap.forEach((k, v) -> {
                HashSet<Tag> tags = new HashSet<>();
                tags.addAll(platformTag.getTags());
                tags.add(Tag.of("name", k));
                if (v instanceof RedisConnectionFactory) {
                    RedisConnectionFactory redisConnectionFactory = (RedisConnectionFactory)v;
                    if (redisConnectionFactory instanceof LettuceConnectionFactory) {
                        new LettucePoolMeterBinder(tags, (LettuceConnectionFactory)redisConnectionFactory)
                            .bindTo(Metrics.globalRegistry);
                    }
                    if (redisConnectionFactory instanceof JedisConnectionFactory) {
                        new JedisPoolMeterBinder(tags, (JedisConnectionFactory)redisConnectionFactory)
                            .bindTo(Metrics.globalRegistry);
                    }
                } else {
                    if (v instanceof LettuceConnectionFactory) {
                        new LettucePoolMeterBinder(tags, (LettuceConnectionFactory)v).bindTo(Metrics.globalRegistry);
                    }
                    if (v instanceof JedisConnectionFactory) {
                        new JedisPoolMeterBinder(tags, (JedisConnectionFactory)v).bindTo(Metrics.globalRegistry);
                    }
                }

            });
        }

    }

}
