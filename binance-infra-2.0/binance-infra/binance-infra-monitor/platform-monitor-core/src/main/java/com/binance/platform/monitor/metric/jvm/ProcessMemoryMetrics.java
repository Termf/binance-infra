package com.binance.platform.monitor.metric.jvm;

import java.util.Locale;

import com.binance.platform.monitor.metric.jvm.procfs.ProcfsSmaps.KEY;
import com.binance.platform.monitor.metric.jvm.procfs.ProcfsStatus;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ProcessMemoryMetrics implements MeterBinder {

    private final ProcfsStatus status;

    private final Iterable<Tag> tags;

    public ProcessMemoryMetrics(Iterable<Tag> tags) {
        this.status = ProcfsStatus.getInstance();
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        for (final KEY key : KEY.values()) {
            final String name = "process.memory." + key.name().toLowerCase(Locale.ENGLISH);
            Gauge.builder(name, status, statusRef -> value(key))//
                .baseUnit("bytes")//
                .tags(tags)//
                .register(registry);
        }
    }

    private Double value(KEY key) {
        return status.get(key);
    }

}