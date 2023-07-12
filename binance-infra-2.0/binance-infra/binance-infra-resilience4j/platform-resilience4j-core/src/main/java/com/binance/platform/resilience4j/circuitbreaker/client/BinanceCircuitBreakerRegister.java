package com.binance.platform.resilience4j.circuitbreaker.client;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

/**
 * author: sait xuc
 */
public class BinanceCircuitBreakerRegister {

    protected final ConcurrentMap<String, CircuitBreaker> entryMap = Maps.newConcurrentMap();
    protected final ConcurrentMap<String, CircuitBreakerConfig> configMap = Maps.newConcurrentMap();

    public synchronized void register(String name, CircuitBreakerConfig config) {
        CircuitBreaker circuitBreaker = this.entryMap.get(name);
        if (circuitBreaker != null) {
            return;
        }
        circuitBreaker = CircuitBreaker.of(name, config);
        this.configMap.putIfAbsent(name, config);
        this.entryMap.putIfAbsent(name, circuitBreaker);
    }

    public CircuitBreaker getOrCreate(String name, CircuitBreakerConfig config) {
        CircuitBreaker circuitBreaker = this.entryMap.get(name);
        if (circuitBreaker == null) {
            this.register(name, config);
            circuitBreaker = this.entryMap.get(name);
        }
        return circuitBreaker;
    }

}
