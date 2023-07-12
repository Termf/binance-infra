package com.binance.platform.openfeign.idempotent.store;

import java.io.Serializable;
import java.util.Map;

import com.binance.platform.openfeign.idempotent.IdempotentRepository;
import com.google.common.collect.Maps;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastIdempotententRepository implements IdempotentRepository {

    private static final String HAZELCASTIDEMPOTENTENTREPOSITORY_KEY = "HAZELCAST_IDEMPOTENTENT_REPOSITORY";

    private HazelcastInstance hz;

    private Map<String, Map<String, Serializable>> IDEMPOTENTENT_CACHE;

    public HazelcastIdempotententRepository(HazelcastInstance hz) {
        this.hz = hz;
        this.IDEMPOTENTENT_CACHE = this.hz.getMap(HAZELCASTIDEMPOTENTENTREPOSITORY_KEY);
    }

    @Override
    public boolean register(String appName, String idempotencyKey, String request) {
        try {
            Map<String, Serializable> idempotencyMap = IDEMPOTENTENT_CACHE.get(appName);
            if (idempotencyKey == null) {
                idempotencyMap = Maps.newHashMap();
            }
            idempotencyMap.put(idempotencyKey, request);
            IDEMPOTENTENT_CACHE.put(appName, idempotencyMap);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean unregister(String appName, String idempotencyKey) {
        try {
            Map<String, Serializable> idempotencyMap = IDEMPOTENTENT_CACHE.get(appName);
            if (idempotencyKey != null) {
                idempotencyMap.remove(idempotencyKey);
            }
            IDEMPOTENTENT_CACHE.put(appName, idempotencyMap);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean exist(String appName, String idempotencyKey) {
        try {
            Map<String, Serializable> idempotencyMap = IDEMPOTENTENT_CACHE.get(appName);
            return idempotencyMap != null && idempotencyMap.containsKey(idempotencyKey);
        } catch (Throwable e) {
            return false;
        }
    }

}
