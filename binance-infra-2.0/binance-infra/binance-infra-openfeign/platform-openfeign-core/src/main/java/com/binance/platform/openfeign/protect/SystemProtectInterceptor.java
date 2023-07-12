package com.binance.platform.openfeign.protect;

import static com.binance.master.error.GeneralCode.SERVICE_HEAVY_LOAD;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.binance.master.models.APIResponse;
import com.binance.platform.common.MyJsonUtil;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;

/**
 * @author james.li
 */
@Slf4j
public class SystemProtectInterceptor extends HandlerInterceptorAdapter implements ConfigChangeListener {

    private static final String SYSTEM_RULE_MAX_THREAD_RATIO = "service.system.maxThreadRatio";// 0 ~ 100

    private static final String SYSTEM_RULE_MAX_CPU_USAGE = "service.system.maxCpuUsage";

    private static final String SYSTEM_RULE_URI_WHITELIST = "service.system.uri.whitelist";

    public static final String SYSTEM_RULE_APP_BLACKLIST = "service.system.app.blacklist";

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemProtectInterceptor.class);

    private final SystemStatusListener statusListener;

    private final LoadingCache<String, Integer> configCache;

    /**
     * The counter for thread count.
     */
    private AtomicInteger curThreadNum = new AtomicInteger(0);

    private Environment env;

    private volatile Set<String> uriWhitelist = new HashSet<>();

    public SystemProtectInterceptor(Environment env, SystemStatusListener statusListener) {
        this.env = env;
        this.statusListener = statusListener;
        configCache = buildCache();
        refreshWhitelist();
    }

    private void refreshWhitelist() {
        String whitelist = env.getProperty(SYSTEM_RULE_URI_WHITELIST);
        if (whitelist != null) {
            String[] urls = whitelist.split("\n");
            Set<String> newSet = new HashSet<>();
            for (String s : urls) {
                newSet.add(s);
            }
            uriWhitelist = newSet;
            log.info("system protect uri whitelist is refreshed. whitelist: {}", uriWhitelist);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // increase threads number for any request
        int curThreads = curThreadNum.incrementAndGet();

        if (uriWhitelist.contains(request.getRequestURI())) {
            // ignore requests that specified in the whitelist
            return true;
        }

        double cpuUsage = statusListener.getCpuUsage();
        if (cpuUsage * 100 > configCache.get(SYSTEM_RULE_MAX_CPU_USAGE)) {
            sendResponse(request, response, "cpu", cpuUsage);
            return false;
        }

        if (curThreads > configCache.get(SYSTEM_RULE_MAX_THREAD_RATIO)) {
            sendResponse(request, response, "thread", curThreads);
            return false;
        }
        return true;
    }

    final String errorResp = MyJsonUtil.toJson(new APIResponse<>(APIResponse.Status.ERROR, APIResponse.Type.GENERAL, SERVICE_HEAVY_LOAD.getCode(),
            SERVICE_HEAVY_LOAD.getMessage(), null));

    private void sendResponse(HttpServletRequest request, HttpServletResponse response, String blockType, double curValue) throws IOException {
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("Warning", "Internal Circuit Breaker");

        try (PrintWriter out = response.getWriter()) {
            Metrics.counter("service_protect_count", "uri", request.getRequestURI(), "blockType", blockType);
            LOGGER.warn("trigger system protect for request: {}@{} blockType: {} curValue: {}", request.getMethod(), request.getRequestURI(),
                    blockType, curValue);
            out.print(errorResp);
        } finally {
            curThreadNum.decrementAndGet();
        }
    }

    private LoadingCache<String, Integer> buildCache() {
        return CacheBuilder.newBuilder().refreshAfterWrite(30, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) throws Exception {
                switch (s) {
                    case SYSTEM_RULE_MAX_THREAD_RATIO:
                        return getMaxThreadsConfig();
                    case SYSTEM_RULE_MAX_CPU_USAGE:
                        return getMaxCpuUsageConfig();
                    default:
                        return Integer.MAX_VALUE;
                }
            }
        });
    }

    private int getMaxThreadsConfig() {
        try {
            int maxThreadRatio = Integer.parseInt(env.getProperty(SYSTEM_RULE_MAX_THREAD_RATIO, "80"));
            if (maxThreadRatio >= 0 && maxThreadRatio <= 100) {
                String maxThreadsStr = env.getProperty("server.tomcat.max-threads", "640");
                int maxConcurrentThreads = (int) (maxThreadRatio / 100.0 * Integer.parseInt(maxThreadsStr));
                return maxConcurrentThreads;
            }
        } catch (Throwable e) {
            LOGGER.warn("got error when parsing service.system.maxThreadRatio", e);
            return Integer.MAX_VALUE;
        }
        return Integer.MAX_VALUE;
    }

    private int getMaxCpuUsageConfig() {
        try {
            int maxCpuUsage = Integer.valueOf(env.getProperty(SYSTEM_RULE_MAX_CPU_USAGE, "-1"));
            return maxCpuUsage < 0 ? Integer.MAX_VALUE : maxCpuUsage;
        } catch (Throwable e) {
            LOGGER.warn("got error when parsing service.system.maxCpuUsage", e);
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        curThreadNum.decrementAndGet();
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(SYSTEM_RULE_URI_WHITELIST)) {
            refreshWhitelist();
        }
    }
}
