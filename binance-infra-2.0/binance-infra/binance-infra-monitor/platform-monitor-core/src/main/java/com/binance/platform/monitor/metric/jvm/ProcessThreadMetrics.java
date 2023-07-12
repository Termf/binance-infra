package com.binance.platform.monitor.metric.jvm;

import com.binance.platform.monitor.metric.jvm.procfs.ProcfsStatus;
import com.binance.platform.monitor.metric.jvm.procfs.ProcfsStatus.KEY;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ProcessThreadMetrics implements MeterBinder {

    private final ProcfsStatus status;
    private final Iterable<Tag> tags;

    public ProcessThreadMetrics(Iterable<Tag> tags) {
        this.status = ProcfsStatus.getInstance();
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder("process.threads", status, statusRef -> value(KEY.THREADS))//
            .description("The number of process threads")//
            .tags(tags)//
            .register(registry);
    }

    private Double value(KEY key) {
        return status.get(key);
    }

}
