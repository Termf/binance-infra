package com.binance.platform.monitor.metric.jvm;

import static java.util.Collections.emptyList;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

import com.sun.management.GarbageCollectionNotificationInfo;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class MyJvmGcMetrics implements MeterBinder, AutoCloseable {

    private final Iterable<Tag> tags;

    private final List<Runnable> notificationListenerCleanUpRunnables = new CopyOnWriteArrayList<>();

    public MyJvmGcMetrics() {
        this(emptyList());
    }

    public MyJvmGcMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        // for safepoint time
        Gauge.builder("jvm.safepoint.count", () -> getSafePoint("count")).tags(tags).description("safepoint time")
            .baseUnit("count").register(registry);
        Gauge.builder("jvm.safepoint.time", () -> getSafePoint("time")).tags(tags).description("safepoint time")
            .baseUnit("time").register(registry);
        Gauge.builder("jvm.safepoint.synctime", () -> getSafePoint("synctime")).tags(tags).description("safepoint time")
            .baseUnit("time").register(registry);

        // start watching for GC notifications
        for (GarbageCollectorMXBean mbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (!(mbean instanceof NotificationEmitter)) {
                continue;
            }
            AtomicLong gcCount = new AtomicLong(0L);
            AtomicLong gcTime = new AtomicLong(0L);
            Gauge.builder("jvm.gc.collection.count", gcCount, AtomicLong::get).tags(tags).tag("gc", mbean.getName())
                .description("gc count").baseUnit("count").register(registry);

            Gauge.builder("jvm.gc.collection.time", gcTime, AtomicLong::get).tags(tags).tag("gc", mbean.getName())
                .description("gc time").baseUnit("time").register(registry);

            NotificationListener notificationListener = (notification, ref) -> {
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }
                gcCount.set(mbean.getCollectionCount());
                gcTime.set(mbean.getCollectionTime());

            };
            NotificationEmitter notificationEmitter = (NotificationEmitter)mbean;
            notificationEmitter.addNotificationListener(notificationListener, null, null);
            notificationListenerCleanUpRunnables.add(() -> {
                try {
                    notificationEmitter.removeNotificationListener(notificationListener);
                } catch (ListenerNotFoundException ignore) {
                }
            });
        }

    }

    private Long getSafePoint(String type) {
        try {
            sun.management.HotspotRuntimeMBean hotspotRuntimeMBean =
                sun.management.ManagementFactoryHelper.getHotspotRuntimeMBean();
            switch (type) {
                case "time":
                    return hotspotRuntimeMBean.getTotalSafepointTime();
                case "count":
                    return hotspotRuntimeMBean.getSafepointCount();
                case "synctime":
                    return hotspotRuntimeMBean.getSafepointSyncTime();
                default:
                    return 0L;
            }
        } catch (Throwable e) {
            return 0L;
        }

    }

    @Override
    public void close() {
        notificationListenerCleanUpRunnables.forEach(Runnable::run);
    }
}
