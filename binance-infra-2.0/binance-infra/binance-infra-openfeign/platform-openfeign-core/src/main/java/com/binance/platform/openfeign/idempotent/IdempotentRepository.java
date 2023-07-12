package com.binance.platform.openfeign.idempotent;

public interface IdempotentRepository {

    /**
     * 注册请求
     */
    boolean register(String appName, String idempotencyKey, String request);

    /**
     * 释放请求
     */
    boolean unregister(String appName, String idempotencyKey);

    /**
     * 查询存在
     */
    boolean exist(String appName, String idempotencyKey);
}
