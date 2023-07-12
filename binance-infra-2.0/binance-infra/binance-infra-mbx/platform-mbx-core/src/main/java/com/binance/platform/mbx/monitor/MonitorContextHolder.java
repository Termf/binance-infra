package com.binance.platform.mbx.monitor;

/**
 * @author Li Fenggang
 * Date: 2020/7/9 6:49 上午
 */
public class MonitorContextHolder {
    private static ThreadLocal<MonitorContext> monitorContextThreadLocal = new ThreadLocal<>();

    public static MonitorContext get() {
        return monitorContextThreadLocal.get();
    }

    public static void set(MonitorContext monitorContext) {
        monitorContextThreadLocal.set(monitorContext);
    }
}
