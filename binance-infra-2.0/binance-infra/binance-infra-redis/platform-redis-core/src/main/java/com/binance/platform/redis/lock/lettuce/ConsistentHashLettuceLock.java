package com.binance.platform.redis.lock.lettuce;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import com.binance.platform.redis.RedisLock;

public class ConsistentHashLettuceLock implements RedisLock {

    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void unlock() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean tryLock(String key, long wait, long expire) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void unlock(String key) {
        // TODO Auto-generated method stub

    }

}
