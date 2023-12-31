package com.binance.platform.monitor.metric.micrometer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.MeterBinder;

public class MicrometerBinder implements MeterBinder {
    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder("micrometer.meters.size", Metrics.globalRegistry, (r) -> {
            return r.getMeters().size();
        }).description("Size of globalRegistry metermap").register(registry);
    }
}
