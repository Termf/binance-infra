package com.binance.platform.mbx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 3:10 下午
 */
@ConfigurationProperties(prefix = MbxCustomProperties.MBX_SDK_PROPERTY_PREFIX)
public class MbxCustomProperties {
    public static final String MBX_SDK_PROPERTY_PREFIX = "com.binance.infa.mbx";
    public static final String CACHE_ACCOUNT_ID_MAX_SIZE_NAME = MBX_SDK_PROPERTY_PREFIX + ".accountIdCacheMaxSize";
    public static final String CACHE_ACCOUNT_ID_MINUTES_NAME = MBX_SDK_PROPERTY_PREFIX + ".accountIdCacheMinutes";
    public static final String CACHE_ACCOUNT_ID_CLEAR_NAME = MBX_SDK_PROPERTY_PREFIX + ".clearAccountIdCache";
    public static final int DEFAULT_ACCOUNT_ID_CACHE_MAX_SIZE = 10000;
    public static final int DEFAULT_ACCOUNT_ID_CACHE_MINUTES = 60;
    /**
     * read timeout of socket in seconds for matchbox.
     */
    private long readTimeoutForMatchbox = 10;
    /**
     * the max idle connections in socket connection pool for matchbox
     */
    private int maxIdleConnectionsForMatchbox = 5;

    /**
     * read timeout of socket in seconds for internal service.
     */
    private long readTimeoutForInternalService = 10;
    /**
     * the max idle connections in socket connection pool for internal service
     */
    private int maxIdleConnectionsForInternalService = 5;
    /**
     * the cache size for accountId
     */
    private int accountIdCacheMaxSize = DEFAULT_ACCOUNT_ID_CACHE_MAX_SIZE;
    /**
     * the cache duration for accountId， in minutes.
     */
    private int accountIdCacheMinutes = DEFAULT_ACCOUNT_ID_CACHE_MINUTES;

    public long getReadTimeoutForMatchbox() {
        return readTimeoutForMatchbox;
    }

    public void setReadTimeoutForMatchbox(long readTimeoutForMatchbox) {
        this.readTimeoutForMatchbox = readTimeoutForMatchbox;
    }

    public int getMaxIdleConnectionsForMatchbox() {
        return maxIdleConnectionsForMatchbox;
    }

    public void setMaxIdleConnectionsForMatchbox(int maxIdleConnectionsForMatchbox) {
        this.maxIdleConnectionsForMatchbox = maxIdleConnectionsForMatchbox;
    }

    public long getReadTimeoutForInternalService() {
        return readTimeoutForInternalService;
    }

    public void setReadTimeoutForInternalService(long readTimeoutForInternalService) {
        this.readTimeoutForInternalService = readTimeoutForInternalService;
    }

    public int getMaxIdleConnectionsForInternalService() {
        return maxIdleConnectionsForInternalService;
    }

    public void setMaxIdleConnectionsForInternalService(int maxIdleConnectionsForInternalService) {
        this.maxIdleConnectionsForInternalService = maxIdleConnectionsForInternalService;
    }

    public int getAccountIdCacheMaxSize() {
        return accountIdCacheMaxSize;
    }

    public void setAccountIdCacheMaxSize(int accountIdCacheMaxSize) {
        this.accountIdCacheMaxSize = accountIdCacheMaxSize;
    }

    public int getAccountIdCacheMinutes() {
        return accountIdCacheMinutes;
    }

    public void setAccountIdCacheMinutes(int accountIdCacheMinutes) {
        this.accountIdCacheMinutes = accountIdCacheMinutes;
    }
}
