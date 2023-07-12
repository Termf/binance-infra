package com.binance.platform.resilience4j.timeLimiter.client;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * author: sait xuc
 */
public class BinanceTimeLimiterRegister {

    protected final ConcurrentMap<String, TimeLimiter> entryMap;
    protected final ConcurrentMap<String, TimeLimiterConfig> configMap;
    //private final ReentrantReadWriteLock lock;

    public BinanceTimeLimiterRegister() {
        this.entryMap = new ConcurrentHashMap();
        this.configMap = new ConcurrentHashMap();
        //this.lock = new ReentrantReadWriteLock();
    }

    private synchronized void register(String name, TimeLimiterConfig config) {
        TimeLimiter timelimiter = this.entryMap.get(name);
        if(timelimiter != null) {
            return;
        }
        timelimiter = TimeLimiter.of(name, config);
        this.configMap.putIfAbsent(name, config);
        this.entryMap.putIfAbsent(name, timelimiter);
        /***
        lock.writeLock().tryLock();
        try {
            TimeLimiter timelimiter = this.entryMap.get(name);
            if(timelimiter != null) {
                return;
            }
            timelimiter = TimeLimiter.of(name, config);
            this.configMap.putIfAbsent(name, config);
            this.entryMap.putIfAbsent(name, timelimiter);
        } catch (Exception exception) {
            //exception.printStackTrace();
        }finally {
            this.lock.writeLock().unlock();;
        }
         ***/
    }

    public TimeLimiter getOrCreate(String name, TimeLimiterConfig config) {
        TimeLimiter timelimiter = this.entryMap.get(name);
        if(timelimiter ==  null) {
            this.register(name, config);
            timelimiter = this.entryMap.get(name);
        }
        return timelimiter;
    }

}
