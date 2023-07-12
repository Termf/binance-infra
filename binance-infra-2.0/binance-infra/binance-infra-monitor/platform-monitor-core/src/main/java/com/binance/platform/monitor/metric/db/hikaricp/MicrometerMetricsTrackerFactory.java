package com.binance.platform.monitor.metric.db.hikaricp;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerMetricsTrackerFactory implements MetricsTrackerFactory {

    private final MeterRegistry registry;

    private final CustomizerTag tag;

    public MicrometerMetricsTrackerFactory(MeterRegistry registry, CustomizerTag tag) {
        this.registry = registry;
        this.tag = tag;
    }

    @Override
    public IMetricsTracker create(String poolName, PoolStats poolStats) {
        return new MicrometerMetricsTracker(poolName, poolStats, registry, tag);
    }
}
