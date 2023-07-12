package com.binance.platform.redis.cache.jetcache;

import java.util.HashMap;
import java.util.Map;

public enum CacheConfig {

    DEFAULT_CACHE_SMALL("defaultCacheSmall", 3, 1000),
    DEFAULT_CACHE_MIDDLE("defaultCacheMiddle", 30, 1000),
    DEFAULT_CACHE_BIG("defaultCacheBig", 60, 5000),
    DEFAULT_CACHE("defaultCache", 30, 1000);

    public static final int DEFAULT_MAXSIZE = 1000;
    public static final int DEFAULT_TTL = 30;

    private String cacheName;
    private int ttl = DEFAULT_TTL; // 过期时间（秒）
    private int maxSize = DEFAULT_MAXSIZE; // 最大數量

    CacheConfig(String cacheName, int ttl, int maxSize) {
        this.cacheName = cacheName;
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    public String getCacheName() {
        return cacheName;
    }

    public int getTtl() {
        return ttl;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public static final Map<String, CacheConfig> NAME_CONFIG_MAPPINGS = new HashMap<>(16);
    static {
        for (CacheConfig cacheConfig : values()) {
            NAME_CONFIG_MAPPINGS.put(cacheConfig.cacheName, cacheConfig);
        }
    }

    public static CacheConfig parseBy(String name) {
        return NAME_CONFIG_MAPPINGS.get(name);
    }

}
