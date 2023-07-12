package com.binance.platform.redis;

import java.util.concurrent.locks.Lock;

public interface RedisLock extends Lock {

    // 默认对10个节点加锁
    public static final int DEFAULT_LOCKS = 3;

    // 默认超时时间
    public static final long DEFAULT_EXPIRE_MILLS = 60000;

    // 默认等待时间
    public static final long DEFAULT_WAIT_MILLS = 3000;

    // 默认的key
    public static final String GLOBAL_LOCK_DEFAULT_KEY = "redis.global.distribute.key";

    boolean tryLock(String key, long wait, long expire);

    void unlock(String key);
}
