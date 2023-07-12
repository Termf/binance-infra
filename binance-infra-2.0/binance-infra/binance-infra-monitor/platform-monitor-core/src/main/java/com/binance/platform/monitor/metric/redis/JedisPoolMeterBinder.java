package com.binance.platform.monitor.metric.redis;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.ReflectionUtils;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

public class JedisPoolMeterBinder implements MeterBinder {

    private static final Logger log = LoggerFactory.getLogger(JedisPoolMeterBinder.class);

    Set<Tag> tags;
    Pool<Jedis> pool;
    JedisCluster jedisCluster;

    public JedisPoolMeterBinder(Set<Tag> tags, JedisConnectionFactory jedisConnectionFactory) {
        this.tags = tags;
        this.tags.add(Tag.of("clientType", "jedis"));
        jedisConnectionFactory.afterPropertiesSet();
        // for pool
        Field poolField = ReflectionUtils.findField(JedisConnectionFactory.class, "pool");
        ReflectionUtils.makeAccessible(poolField);
        try {
            this.pool = (Pool<Jedis>)poolField.get(jedisConnectionFactory);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        // for cluster
        Field clusterField = ReflectionUtils.findField(JedisConnectionFactory.class, "cluster");
        ReflectionUtils.makeAccessible(clusterField);
        try {
            this.jedisCluster = (JedisCluster)clusterField.get(jedisConnectionFactory);
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
                GenericObjectPool<Jedis> internalPool = (GenericObjectPool<Jedis>)internalPoolField.get(this.pool);
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
        }

        if (jedisCluster != null) {
            Map<String, JedisPool> clusterPool = jedisCluster.getClusterNodes();
            Field internalPoolField = ReflectionUtils.findField(Pool.class, "internalPool");
            ReflectionUtils.makeAccessible(internalPoolField);
            clusterPool.forEach((k, v) -> {
                try {
                    tags.add(Tag.of("cluster", k));
                    GenericObjectPool<Jedis> internalPool = (GenericObjectPool<Jedis>)internalPoolField.get(v);
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
                        .description("The total number of objects successfully borrowed from this pool")
                        .register(registry);

                    Gauge.builder("redis.pool.created", internalPool, GenericObjectPool::getCreatedCount).tags(tags)
                        .description("The total number of objects created for this pool ").register(registry);

                    Gauge.builder("redis.pool.destroyed", internalPool, GenericObjectPool::getDestroyedCount).tags(tags)
                        .description("The total number of objects destroyed by this pool").register(registry);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
            });

        }

    }
}
