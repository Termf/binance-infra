package com.binance.master.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class ThreadUtils {

    private final static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    public static void removeAll() {
        threadLocal.remove();
    }

    public static void remove(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map != null) {
            map.remove(key);
        }
    }

    /**
     * 从当前线程中根据key获取某个值
     * 
     * @param key
     * @return
     */
    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static void each(BiConsumer<String, Object> action) {
        Map<String, Object> map = threadLocal.get();
        if (map != null) {
            map.forEach(action);
        }
    }

    /**
     * 将key、value对设置到当前线程中
     * 
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        threadLocal.set(map);
    }
}
