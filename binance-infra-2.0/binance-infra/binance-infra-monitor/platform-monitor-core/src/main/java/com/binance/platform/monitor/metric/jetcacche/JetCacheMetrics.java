package com.binance.platform.monitor.metric.jetcacche;

import com.alicp.jetcache.anno.support.CacheMonitorManager;
import com.alicp.jetcache.anno.support.DefaultCacheMonitorManager;
import com.alicp.jetcache.support.CacheStat;
import com.binance.platform.monitor.Monitors;
import com.binance.platform.monitor.metric.CustomizerTag;
import com.vip.vjtools.vjkit.net.NetUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


public class JetCacheMetrics {

    private final CustomizerTag tag;

    private final Map<String, CacheMonitorManager> cacheMonitorManagerMap;

    public JetCacheMetrics(Map<String, CacheMonitorManager> cacheMonitorManagerMap, CustomizerTag tag) {
        this.tag = tag;
        this.cacheMonitorManagerMap = cacheMonitorManagerMap;
    }

    public void init() {
        if (!CollectionUtils.isEmpty(cacheMonitorManagerMap)) {
            cacheMonitorManagerMap.forEach((k, v) -> {
                if (v instanceof DefaultCacheMonitorManager) {
                    DefaultCacheMonitorManager defaultCacheMonitorManager = (DefaultCacheMonitorManager) v;
                    defaultCacheMonitorManager.setMetricsCallback((statInfo) -> {
                        List<CacheStat> cacheStats = statInfo.getStats();
                        for (CacheStat cacheStat : cacheStats) {

                            Monitors.count("jetcache.stats.getCount", cacheStat.getGetCount(), "application", tag.getApplication(), "instance",
                                    NetUtil.getLocalHost(), "cacheName", cacheStat.getCacheName());
                            Monitors.count("jetcache.stats.getHitCount", cacheStat.getGetHitCount(), "application", tag.getApplication(), "instance",
                                    NetUtil.getLocalHost(), "cacheName", cacheStat.getCacheName());
                            Monitors.count("jetcache.stats.getMissCount", cacheStat.getGetMissCount(), "application", tag.getApplication(),
                                    "instance", NetUtil.getLocalHost(), "cacheName", cacheStat.getCacheName());
                            Monitors.count("jetcache.stats.getFailCount", cacheStat.getGetFailCount(), "application", tag.getApplication(),
                                    "instance", NetUtil.getLocalHost(), "cacheName", cacheStat.getCacheName());
                            Monitors.count("jetcache.stats.qps", cacheStat.qps(), "application", tag.getApplication(), "instance",
                                    NetUtil.getLocalHost(), "cacheName", cacheStat.getCacheName());

                        }
                    });
                    defaultCacheMonitorManager.shutdown();
                    defaultCacheMonitorManager.init();
                }
            });
        }
    }

}
