package com.binance.platform.mbx.constant;

public interface MetricsConstants {
    // metric
    /** metric name - sdk timer */
    String METRICS_LATENCY_SDK = "mbx.latency.sdk";
    /** metric name - mbx rpc timer */
    String METRICS_LATENCY_RPC_MBX = "mbx.latency.rpc.mbx";
    /** metric name - accountId cache total */
    String METRICS_CACHE_TOTAL = "mbx.sdk.cache.total";
    /** metric name - accountId cache miss */
    String METRICS_CACHE_MISS = "mbx.sdk.cache.miss";
    /** metric name - cache size */
    String METRICS_CACHE_SIZE = "mbx.sdk.cache.size";

    // TAG
    String TAG_NAME_ENTRANCE = "entrance";
    String TAG_NAME_SPAN = "span";
    String TAG_NAME_MBX_URI = "mbx.uri";
    String TAG_NAME_MBX_METHOD = "mbx.method";
    String TAG_NAME_HTTP_CODE = "http.code";
    String TAG_NAME_EXCEPTION = "ex";
    // cache
    String TAG_NAME_CACHE_NAME = "cache";
}
