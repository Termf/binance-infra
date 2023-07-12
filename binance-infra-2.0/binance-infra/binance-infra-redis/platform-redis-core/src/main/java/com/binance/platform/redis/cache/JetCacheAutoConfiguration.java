package com.binance.platform.redis.cache;

import java.util.Map;
import java.util.function.Function;

import com.alicp.jetcache.anno.support.*;
import com.binance.platform.redis.cache.jetcache.CacheConfig;
import com.binance.platform.redis.cache.jetcache.JetCacheProperties;
import com.binance.platform.redis.cache.jetcache.RedisDataCacheBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisUseJedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

import com.alicp.jetcache.AbstractCacheBuilder;
import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.SerialPolicy;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.embedded.EmbeddedCacheBuilder;
import com.alicp.jetcache.external.ExternalCacheBuilder;
import com.binance.platform.redis.cache.jetcache.JetCacheManager;
import com.binance.platform.redis.cache.support.JacksonKeyConverter;
import com.google.common.collect.Maps;

@Configuration
@ConditionalOnProperty(prefix = "jetcache", name = "enable", havingValue = "true")
@ConditionalOnClass({RedisOperations.class, GlobalCacheConfig.class})
@Import({RedisUseJedisAutoConfiguration.class})
@EnableConfigurationProperties(JetCacheProperties.class)
@EnableMethodCache(basePackages = "com.binance")
public class JetCacheAutoConfiguration {

    public static final String GLOBAL_CACHE_CONFIG_NAME = "globalCacheConfig";

    public static final String LOCAL_CACHE_CONFIG_PREFIX = "jetcache.local.";

    public static final String REMOTE_CACHE_CONFIG_PREFIX = "jetcache.remote.";

    public static final String REMOTE_DEFAULT_KEY_PREFIX = "jetcache.";

    @Autowired
    protected ConfigurableEnvironment environment;

    @Bean
    @ConditionalOnMissingBean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheMonitorManager cacheMonitorManager() {
        DefaultCacheMonitorManager defaultCacheMonitorManager = new DefaultCacheMonitorManager();
        defaultCacheMonitorManager.setMetricsCallback((statInfo) -> {
            // 默认为空
        });
        return defaultCacheMonitorManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public EmbeddedCacheBuilder embeddedcachebuilder(SpringConfigProvider configProvider) {
        ConfigTree ct = new ConfigTree(environment, LOCAL_CACHE_CONFIG_PREFIX).subTree("default.");
        CaffeineCacheBuilder builder = CaffeineCacheBuilder.createCaffeineCacheBuilder();
        parseGeneralConfig(builder, configProvider, ct);
        builder.limit(Integer.parseInt(ct.getProperty("limit", String.valueOf(CacheConfig.DEFAULT_MAXSIZE))));
        return builder;
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean
    public ExternalCacheBuilder externalCacheBuilder(SpringConfigProvider configProvider,
                                                     @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        ConfigTree ct = new ConfigTree(environment, REMOTE_CACHE_CONFIG_PREFIX).subTree("default.");
        ExternalCacheBuilder builder = RedisDataCacheBuilder.createBuilder().connectionFactory(redisConnectionFactory);
        parseGeneralConfig(builder, configProvider, ct);
        String defaultKeyPrefix =
                new StringBuilder(REMOTE_DEFAULT_KEY_PREFIX).append(environment.getProperty("spring.application.name")).append(".").toString();
        builder.setKeyPrefix(ct.getProperty("keyPrefix", defaultKeyPrefix));
        builder.setValueEncoder(configProvider.parseValueEncoder(ct.getProperty("valueEncoder", SerialPolicy.JAVA)));
        builder.setValueDecoder(configProvider.parseValueDecoder(ct.getProperty("valueDecoder", SerialPolicy.JAVA)));
        return builder;
    }

    @Bean(name = GLOBAL_CACHE_CONFIG_NAME)
    @ConditionalOnMissingBean
    public GlobalCacheConfig globalCacheConfig(JetCacheProperties props, EmbeddedCacheBuilder embeddedCacheBuilder,
                                               ObjectProvider<ExternalCacheBuilder> externalCacheBuilderProvider) {
        Map<String, CacheBuilder> localCacheBuilders = Maps.newHashMap();
        Map<String, CacheBuilder> remoteCacheBuilders = Maps.newHashMap();
        localCacheBuilders.put(CacheConsts.DEFAULT_AREA, embeddedCacheBuilder);
        remoteCacheBuilders.put(CacheConsts.DEFAULT_AREA, externalCacheBuilderProvider.getIfAvailable());
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setHiddenPackages(props.getHiddenPackages());
        globalCacheConfig.setStatIntervalMinutes(props.getStatIntervalMinutes() == 0 ? 1 : props.getStatIntervalMinutes());
        globalCacheConfig.setAreaInCacheName(props.isAreaInCacheName());
        globalCacheConfig.setPenetrationProtect(props.isPenetrationProtect());
        globalCacheConfig.setEnableMethodCache(props.isEnableMethodCache());
        globalCacheConfig.setLocalCacheBuilders(localCacheBuilders);
        globalCacheConfig.setRemoteCacheBuilders(remoteCacheBuilders);
        return globalCacheConfig;
    }

    @Bean
    public JetCacheManager jetCacheManager(SpringConfigProvider configProvider, EmbeddedCacheBuilder embeddedCacheBuilder,
                                           ObjectProvider<ExternalCacheBuilder> externalCacheBuilderProvider) {
        JetCacheManager jetCacheManager = new JetCacheManager();
        jetCacheManager.setConfigProvider(configProvider);
        jetCacheManager.setEmbeddedCacheBuilder(embeddedCacheBuilder);
        jetCacheManager.setExternalCacheBuilder(externalCacheBuilderProvider.getIfAvailable());
        return jetCacheManager;
    }

    private static void parseGeneralConfig(CacheBuilder builder, ConfigProvider configProvider, ConfigTree ct) {
        AbstractCacheBuilder acb = (AbstractCacheBuilder) builder;
        // 全局默认使用jackson做keyConvertor
        Function<Object, Object> keyConvertor = configProvider.parseKeyConvertor(ct.getProperty("keyConvertor"));
        acb.keyConvertor(keyConvertor == null ? JacksonKeyConverter.INSTANCE : keyConvertor);
        // 全局过默认期时间配置
        String expireAfterWriteInMillis = ct.getProperty("expireAfterWriteInMillis");
        if (expireAfterWriteInMillis == null) {
            expireAfterWriteInMillis = ct.getProperty("defaultExpireInMillis");
        }
        if (expireAfterWriteInMillis != null) {
            acb.setExpireAfterWriteInMillis(Long.parseLong(expireAfterWriteInMillis));
        } else {
            // 默认全局失效时间为30s
            acb.setExpireAfterWriteInMillis(30000L);
        }
        String expireAfterAccessInMillis = ct.getProperty("expireAfterAccessInMillis");
        if (expireAfterAccessInMillis != null) {
            acb.setExpireAfterAccessInMillis(Long.parseLong(expireAfterAccessInMillis));
        }
    }


    static class ConfigTree {

        private final ConfigurableEnvironment environment;
        private final String prefix;

        public ConfigTree(ConfigurableEnvironment environment, String prefix) {
            Assert.notNull(environment, "environment is required");
            Assert.notNull(prefix, "prefix is required");
            this.environment = environment;
            this.prefix = prefix;
        }

        public ConfigTree subTree(String prefix) {
            return new ConfigTree(environment, fullPrefixOfKey(prefix));
        }

        public boolean containsProperty(String key) {
            return environment.containsProperty(fullPrefixOfKey(key));
        }

        public <T> T getProperty(String key) {
            return (T) environment.getProperty(fullPrefixOfKey(key));
        }

        private String fullPrefixOfKey(String prefixOrKey) {
            return this.prefix + prefixOrKey;
        }

        public <T> T getProperty(String key, T defaultValue) {
            if (containsProperty(key)) {
                return getProperty(key);
            } else {
                return defaultValue;
            }
        }
    }

}
