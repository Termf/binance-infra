package com.binance.platform.monitor.takin;

import javax.servlet.http.HttpServletRequest;

public class TestContext {

    //默认不是测试流量，takin agent 会做相关增强。
    public static boolean isTest() {
        return false;
    }

    public static boolean isTestRequest(HttpServletRequest servletRequest) {
        if (servletRequest.getHeader("Takin-Agent") != null) {
            if ("PerfomanceTest".equals(servletRequest.getHeader("Takin-Agent")) || "PerformanceTest".equals(servletRequest.getHeader("Takin-Agent"))) {
                return true;
            }
        }
        return false;
    }

    //默认takin agent 没有就绪， takin agnet会做相关增强，返回是否就绪。
    public static boolean isTakinReady() {
        return false;
    }
}
