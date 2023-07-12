package com.binance.platform.monitor.metric.redis;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePool;
import org.springframework.util.ReflectionUtils;

import io.lettuce.core.api.StatefulConnection;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import redis.clients.util.Pool;

public class LettucePoolMeterBinder implements MeterBinder {

    private static final Logger log = LoggerFactory.getLogger(LettucePoolMeterBinder.class);

    Set<Tag> tags;
    LettucePool pool;
    LettuceConnectionFactory lettuceConnectionFactory;

    public LettucePoolMeterBinder(Set<Tag> tags, LettuceConnectionFactory lettuceConnectionFactory) {
        this.tags = tags;
        this.tags.add(Tag.of("clientType", "lettuce"));
        this.lettuceConnectionFactory = lettuceConnectionFactory;
        lettuceConnectionFactory.afterPropertiesSet();
        Field poolField = ReflectionUtils.findField(LettuceConnectionFactory.class, "pool");
        ReflectionUtils.makeAccessible(poolField);
        try {
            this.pool = (LettucePool)poolField.get(lettuceConnectionFactory);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (pool != null) {
            Field internalPoolField = ReflectionUtils.findField(Pool.class, "internalPool");
            ReflectionUtils.makeAccessible(internalPoolField);
            try {
                GenericObjectPool<StatefulConnection> internalPool =
                    (GenericObjectPool<StatefulConnection>)internalPoolField.get(this.pool);
                Gauge.builder("redis.pool.active", internalPool, GenericObjectPool::getNumActive).tags(tags)
                    .description("Active redis connection").register(registry);

                Gauge.builder("redis.pool.total", internalPool, GenericObjectPool::getMaxTotal).tags(tags)
                    .description("MaxTotal redis connection").register(registry);

                Gauge.builder("redis.pool.idle", internalPool, GenericObjectPool::getNumIdle).tags(tags)
                    .description("Idle redis connection").register(registry);

                Gauge.builder("redis.pool.waiters", internalPool, GenericObjectPool::getNumWaiters).tags(tags)
                    .description(
                        "The estimate of the number of threads currently blocked waiting for an object from the pool")
                    .register(registry);

                Gauge.builder("redis.pool.borrowed", internalPool, GenericObjectPool::getBorrowedCount).tags(tags)
                    .description("The total number of objects successfully borrowed from this pool").register(registry);

                Gauge.builder("redis.pool.created", internalPool, GenericObjectPool::getCreatedCount).tags(tags)
                    .description("The total number of objects created for this pool ").register(registry);

                Gauge.builder("redis.pool.destroyed", internalPool, GenericObjectPool::getDestroyedCount).tags(tags)
                    .description("The total number of objects destroyed by this pool").register(registry);

            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        } else if (lettuceConnectionFactory.isClusterAware()) {
            log.warn(" have not support lettuce cluster");
        }

    }

}
