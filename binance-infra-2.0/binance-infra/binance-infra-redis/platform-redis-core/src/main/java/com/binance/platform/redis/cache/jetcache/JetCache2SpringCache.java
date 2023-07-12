package com.binance.platform.redis.cache.jetcache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.support.CacheEncodeException;
import com.binance.master.utils.JsonUtils;
import com.binance.master.utils.StringUtils;
import com.binance.platform.redis.cache.support.JacksonValueConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class JetCache2SpringCache extends AbstractValueAdaptingCache {

    private final Cache cache;

    private final String cacheName;

    private final JacksonValueConverter jacksonValueConverter;

    private static Logger log = LoggerFactory.getLogger(JetCache2SpringCache.class);

    public JetCache2SpringCache(String cacheName, Cache cache, boolean allowNullValues, JacksonValueConverter jacksonValueConverter) {
        super(allowNullValues);
        Assert.notNull(cacheName, "Name must not be null");
        Assert.notNull(cache, "Cache must not be null");
        this.cache = cache;
        this.cacheName = cacheName;
        this.jacksonValueConverter = jacksonValueConverter;
    }

    @Override
    protected Object lookup(Object key) {
        Object value = this.cache.get(key);
        if (value == null) {
            return null;
        }
        return deserializeCacheValue(value);
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public com.alicp.jetcache.Cache getNativeCache() {
        return cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper result = get(key);
        if (result != null) {
            return (T) result.get();
        }
        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String
                    .format("Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")'.", cacheName));
        }
        try {
            this.cache.PUT(key, serializeCacheValue(cacheValue, false));
        } catch (CacheEncodeException e) {//有些实现了Serializable接口的对象嵌套着没有实现Serializable的对象会java序列化失败,强制使用jackson序列化
            this.cache.PUT(key, serializeCacheValue(cacheValue, true));
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }
        Object result;
        PutIfAbsentFunction callable;
        try {
            callable = new PutIfAbsentFunction(cacheValue, false);
            result = this.cache.computeIfAbsent(key, callable);
        } catch (CacheEncodeException e) {
            callable = new PutIfAbsentFunction(cacheValue, true);
            result = this.cache.computeIfAbsent(key, callable);
        }
        return (callable.called ? null : toValueWrapper(result));
    }

    @Override
    public void evict(Object key) {
        this.cache.remove(key);
    }

    @Override
    public void clear() {
        // TODO 目前jetcache没有实现这个接口
        log.error("unSupport clear operation");
    }

    private class PutIfAbsentFunction implements Function<Object, Object> {

        private final Object value;

        private final boolean forceUsingJackson;

        private boolean called;

        public PutIfAbsentFunction(Object value, boolean forceUsingJackson) {
            this.value = value;
            this.forceUsingJackson = forceUsingJackson;
        }

        @Override
        public Object apply(Object key) {
            this.called = true;
            return serializeCacheValue(this.value, this.forceUsingJackson);
        }
    }


    @Nullable
    protected Object preProcessCacheValue(@Nullable Object value) {
        if (value != null) {
            return value;
        }
        return isAllowNullValues() ? NullValue.INSTANCE : null;
    }

    protected Object serializeCacheValue(Object value, boolean forceUsingJackson) {
        if (isAllowNullValues() && value instanceof NullValue) {
            return NullValue.INSTANCE.toString();
        }
        if (forceUsingJackson || !(value instanceof Serializable)) { //对象没有实现序列化接口时，使用jackson序列化方式
            return jacksonValueConverter.serialize(value);
        } else { //不序列化，通过JetCache使用java序列化方式来序列化
            return value;
        }
    }

    @Nullable
    protected Object deserializeCacheValue(Object value) {
        if (isAllowNullValues() && ObjectUtils.nullSafeEquals(value, NullValue.INSTANCE.toString())) {
            return NullValue.INSTANCE;
        }
        if (value instanceof String && JsonUtils.isJson((String) value) && StringUtils.contains((String) value, JacksonValueConverter.CLASS_IDENTIFIER)) {
            return jacksonValueConverter.deserialize((String) value);
        } else {
            return value;
        }
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {
        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

}
