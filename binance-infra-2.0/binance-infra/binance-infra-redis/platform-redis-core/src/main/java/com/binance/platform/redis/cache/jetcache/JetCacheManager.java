package com.binance.platform.redis.cache.jetcache;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.binance.platform.redis.cache.support.JacksonValueConverter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.alicp.jetcache.MultiLevelCacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.support.ConfigProvider;
import com.alicp.jetcache.anno.support.SimpleCacheManager;
import com.alicp.jetcache.embedded.EmbeddedCacheBuilder;
import com.alicp.jetcache.external.ExternalCacheBuilder;
import com.binance.master.utils.StringUtils;

public class JetCacheManager implements CacheManager {

    private final SimpleCacheManager cacheManager = new SimpleCacheManager();

    private final Set<String> cacheNames = new LinkedHashSet<>(16);

    private ConfigProvider configProvider;

    private EmbeddedCacheBuilder embeddedCacheBuilder;

    private ExternalCacheBuilder externalCacheBuilder;

    private final static JacksonValueConverter jacksonValueConverter = new JacksonValueConverter();

    @Override
    public Cache getCache(String name) {
        com.alicp.jetcache.Cache jetCache = cacheManager.getCacheWithoutCreate(CacheConsts.DEFAULT_AREA, name);
        if (jetCache == null) {
            synchronized (this) {
                jetCache = cacheManager.getCacheWithoutCreate(CacheConsts.DEFAULT_AREA, name);
                if (jetCache == null) {
                    // 定义几个通用的枚举类型提供给上层应用使用，包含默认的配置
                    jetCache = buildCache(CacheConfig.parseBy(name), name);
                    cacheManager.putCache(CacheConsts.DEFAULT_AREA, name, jetCache);
                    this.cacheNames.add(name);
                }
            }
        }
        Cache cache = new JetCache2SpringCache(name, jetCache, true, jacksonValueConverter);
        return cache;
    }

    protected com.alicp.jetcache.Cache buildCache(CacheConfig cacheConfig, String cacheName) {
        com.alicp.jetcache.Cache local = buildLocal(cacheConfig);
        com.alicp.jetcache.Cache remote = buildRemote(cacheConfig, cacheName);
        com.alicp.jetcache.Cache cache = null;
        if (remote != null && local != null) {
            cache = MultiLevelCacheBuilder.createMultiLevelCacheBuilder()
                    .expireAfterWrite(remote.config().getExpireAfterWriteInMillis(), TimeUnit.MILLISECONDS).addCache(local, remote)
                    .useExpireOfSubCache(true).cacheNullValue(false).buildCache();
        } else {
            cache = local;
        }
        cache.config().setCachePenetrationProtect(configProvider.getGlobalCacheConfig().isPenetrationProtect());
        if (configProvider.getCacheMonitorManager() != null) {
            configProvider.getCacheMonitorManager().addMonitors(CacheConsts.DEFAULT_AREA, cacheName, cache);
        }
        return cache;
    }

    protected com.alicp.jetcache.Cache buildLocal(CacheConfig cacheConfig) {
        if (embeddedCacheBuilder == null) {
            return null;
        }
        EmbeddedCacheBuilder localEmbeddedCacheBuilder = (EmbeddedCacheBuilder) embeddedCacheBuilder.clone();
        if (cacheConfig != null) {
            localEmbeddedCacheBuilder.setLimit(cacheConfig.getMaxSize());
            localEmbeddedCacheBuilder.setExpireAfterWriteInMillis(cacheConfig.getTtl() * 1000);
        }
        return localEmbeddedCacheBuilder.buildCache();
    }

    protected com.alicp.jetcache.Cache buildRemote(CacheConfig cacheConfig, String cacheName) {
        if (externalCacheBuilder == null) {
            return null;
        }
        RedisDataCacheBuilder remoteExternalCacheBuilder = (RedisDataCacheBuilder) externalCacheBuilder.clone();
        if (cacheConfig != null) {
            remoteExternalCacheBuilder.setExpireAfterWriteInMillis(cacheConfig.getTtl() * 1000);
        }
        String keyPrefix = remoteExternalCacheBuilder.getConfig().getKeyPrefix();
        if (StringUtils.isNotBlank(cacheName)) {
            remoteExternalCacheBuilder.setKeyPrefix(keyPrefix + cacheName + ".");
        }
        //序列化失败时抛出异常
        remoteExternalCacheBuilder.setSerializedFailThrowException(true);
        return remoteExternalCacheBuilder.buildCache();
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheNames);
    }

    public void setConfigProvider(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public void setEmbeddedCacheBuilder(EmbeddedCacheBuilder embeddedCacheBuilder) {
        this.embeddedCacheBuilder = embeddedCacheBuilder;
    }

    public void setExternalCacheBuilder(ExternalCacheBuilder externalCacheBuilder) {
        this.externalCacheBuilder = externalCacheBuilder;
    }
}
