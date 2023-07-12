package com.binance.platform.resilience4j.ratelimiter.client;


import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * author: sait xuc
 */
public class BinanceRateLimiterRegister {

    protected final ConcurrentMap<String, RateLimiter> entryMap;
    protected final ConcurrentMap<String, RateLimiterConfig> configMap;
    //private final ReentrantReadWriteLock lock;

    public BinanceRateLimiterRegister() {
        this.entryMap = new ConcurrentHashMap();
        this.configMap = new ConcurrentHashMap();
        //this.lock = new ReentrantReadWriteLock();
    }

    public synchronized void register(String name, RateLimiterConfig config) {
        RateLimiter rateLimiter = this.entryMap.get(name);
        if(rateLimiter !=  null) {
            return;
        }
        rateLimiter = RateLimiter.of(name, config);
        this.configMap.putIfAbsent(name, config);
        this.entryMap.putIfAbsent(name, rateLimiter);
        /***
        lock.writeLock().tryLock();
        try {
            RateLimiter rateLimiter = RateLimiter.of(name, config);
            this.configMap.putIfAbsent(name, config);
            this.entryMap.putIfAbsent(name, rateLimiter);
        }catch (Exception exception) {
            //exception.printStackTrace();
        }finally {
            this.lock.writeLock().unlock();;
        }
         ***/

    }

    public RateLimiter getOrCreate(String name, RateLimiterConfig config) {
        RateLimiter rateLimiter = this.entryMap.get(name);
        if(rateLimiter ==  null) {
            this.register(name, config);
            rateLimiter = this.entryMap.get(name);
        }
        return rateLimiter;
    }

}
