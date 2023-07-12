package com.binance.platform.redis.cache.jetcache;

import com.alicp.jetcache.external.ExternalCacheBuilder;
import com.alicp.jetcache.redis.springdata.RedisSpringDataCache;
import com.alicp.jetcache.redis.springdata.RedisSpringDataCacheConfig;
import com.alicp.jetcache.support.CacheEncodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Copy from jetCache RedisSpringDataCacheBuilder,just for deal with Java Encode error.
 */
public class RedisDataCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {

    private boolean serializedFailThrowException = false;

    public static class RedisSpringDataCacheBuilderImpl extends RedisDataCacheBuilder<RedisSpringDataCacheBuilderImpl> {
    }

    public static RedisSpringDataCacheBuilderImpl createBuilder() {
        return new RedisSpringDataCacheBuilderImpl();
    }


    protected RedisDataCacheBuilder() {
        buildFunc(config -> new RedisSpringDataCache((RedisSpringDataCacheConfig) config) {
            private Logger logger = LoggerFactory.getLogger(RedisSpringDataCache.class);

            @Override
            protected void logError(String oper, Object key, Throwable e) {
                if (e instanceof CacheEncodeException) {
                    //java序列化失败，打印warn级别的日志，并且抛出异常
                    logJavaEncodeError(oper, key, e);
                    if (serializedFailThrowException) {
                        throw new CacheEncodeException(e.getMessage(), e);
                    }
                } else {
                    super.logError(oper, key, e);
                }
            }

            private void logJavaEncodeError(String oper, Object key, Throwable e) {
                StringBuilder sb = new StringBuilder(64);
                sb.append("jetcache(")
                        .append(this.getClass().getSimpleName()).append(") ")
                        .append(oper)
                        .append(" error.");
                if (!(key instanceof byte[])) {
                    try {
                        sb.append(" key=[")
                                .append(config().getKeyConvertor().apply(key))
                                .append(']');
                    } catch (Exception ex) {
                        // ignore
                    }
                }
                sb.append(' ');
                if (e != null) {
                    sb.append(e.getClass().getName());
                    sb.append(':');
                    sb.append(e.getMessage());
                }
                logger.warn(sb.toString());
            }

        });
    }

    @Override
    public RedisSpringDataCacheConfig getConfig() {
        if (config == null) {
            config = new RedisSpringDataCacheConfig();
        }
        return (RedisSpringDataCacheConfig) config;
    }

    public T connectionFactory(RedisConnectionFactory connectionFactory) {
        getConfig().setConnectionFactory(connectionFactory);
        return self();
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        getConfig().setConnectionFactory(connectionFactory);
    }

    public void setSerializedFailThrowException(boolean serializedFailThrowException) {
        this.serializedFailThrowException = serializedFailThrowException;
    }
}
