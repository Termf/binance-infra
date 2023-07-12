package io.micrometer.core.instrument.binder.jvm;

import static io.micrometer.core.instrument.binder.jvm.JvmMemory.getOldGen;
import static io.micrometer.core.instrument.binder.jvm.JvmMemory.getUsageValue;
import static io.micrometer.core.instrument.binder.jvm.JvmMemory.isOldGenPool;
import static io.micrometer.core.instrument.binder.jvm.JvmMemory.isYoungGenPool;
import static java.util.Collections.emptyList;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.lang.NonNullApi;
import io.micrometer.core.lang.Nullable;
import io.micrometer.core.util.internal.logging.InternalLogger;
import io.micrometer.core.util.internal.logging.InternalLoggerFactory;

/**
 * 覆盖micrometer的JvmGcMetrics，针对zgc有bug
 * 
 * @author liushiming
 * @date 2020/12/09
 */
public class JvmGcMetrcisEnhance extends JvmGcMetrics {
    private final static InternalLogger log = InternalLoggerFactory.getInstance(JvmGcMetrics.class);

    private final boolean managementExtensionsPresent = isManagementExtensionsPresent();

    private final Iterable<Tag> tags;

    @Nullable
    private String youngGenPoolName;

    @Nullable
    private String oldGenPoolName;

    private final List<Runnable> notificationListenerCleanUpRunnables = new CopyOnWriteArrayList<>();

    public JvmGcMetrcisEnhance() {
        this(emptyList());
    }

    public JvmGcMetrcisEnhance(Iterable<Tag> tags) {
        for (MemoryPoolMXBean mbean : ManagementFactory.getMemoryPoolMXBeans()) {
            String name = mbean.getName();
            if (isYoungGenPool(name)) {
                youngGenPoolName = name;
            } else if (isOldGenPool(name)) {
                oldGenPoolName = name;
            }
        }
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (!this.managementExtensionsPresent) {
            return;
        }

        double maxOldGen = getOldGen().map(mem -> getUsageValue(mem, MemoryUsage::getMax)).orElse(0.0);

        AtomicLong maxDataSize = new AtomicLong((long)maxOldGen);
        Gauge.builder("jvm.gc.max.data.size", maxDataSize, AtomicLong::get).tags(tags)
            .description("Max size of old generation memory pool").baseUnit(BaseUnits.BYTES).register(registry);

        AtomicLong liveDataSize = new AtomicLong(0L);

        Gauge.builder("jvm.gc.live.data.size", liveDataSize, AtomicLong::get).tags(tags)
            .description("Size of old generation memory pool after a full GC").baseUnit(BaseUnits.BYTES)
            .register(registry);

        Counter promotedBytes = Counter.builder("jvm.gc.memory.promoted").tags(tags).baseUnit(BaseUnits.BYTES)
            .description(
                "Count of positive increases in the size of the old generation memory pool before GC to after GC")
            .register(registry);

        Counter allocatedBytes = Counter.builder("jvm.gc.memory.allocated").tags(tags).baseUnit(BaseUnits.BYTES)
            .description(
                "Incremented for an increase in the size of the young generation memory pool after one GC to before the next")
            .register(registry);

        // start watching for GC notifications
        final AtomicLong youngGenSizeAfter = new AtomicLong(0L);

        for (GarbageCollectorMXBean mbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (!(mbean instanceof NotificationEmitter)) {
                continue;
            }
            NotificationListener notificationListener = (notification, ref) -> {
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }
                CompositeData cd = (CompositeData)notification.getUserData();
                GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from(cd);

                String gcCause = notificationInfo.getGcCause();
                String gcAction = notificationInfo.getGcAction();
                GcInfo gcInfo = notificationInfo.getGcInfo();
                long duration = gcInfo.getDuration();
                if (isConcurrentPhase(notificationInfo)) {
                    Timer.builder("jvm.gc.concurrent.phase.time").tags(tags).tags("action", gcAction, "cause", gcCause)
                        .description("Time spent in concurrent phase").register(registry)
                        .record(duration, TimeUnit.MILLISECONDS);
                } else {
                    Timer.builder("jvm.gc.pause").tags(tags).tags("action", gcAction, "cause", gcCause)
                        .description("Time spent in GC pause").register(registry)
                        .record(duration, TimeUnit.MILLISECONDS);
                }

                // Update promotion and allocation counters
                final Map<String, MemoryUsage> before = gcInfo.getMemoryUsageBeforeGc();
                final Map<String, MemoryUsage> after = gcInfo.getMemoryUsageAfterGc();

                if (oldGenPoolName != null) {
                    final long oldBefore = before.get(oldGenPoolName).getUsed();
                    final long oldAfter = after.get(oldGenPoolName).getUsed();
                    final long delta = oldAfter - oldBefore;
                    if (delta > 0L) {
                        promotedBytes.increment(delta);
                    }

                    // Some GC implementations such as G1 can reduce the old gen size as part of a minor GC. To track
                    // the
                    // live data size we record the value if we see a reduction in the old gen heap size or
                    // after a major GC.
                    if (oldAfter < oldBefore
                        || GcGenerationAge.fromName(notificationInfo.getGcName()) == GcGenerationAge.OLD) {
                        liveDataSize.set(oldAfter);
                        final long oldMaxAfter = after.get(oldGenPoolName).getMax();
                        maxDataSize.set(oldMaxAfter);
                    }
                }

                if (youngGenPoolName != null) {
                    final long youngBefore = before.get(youngGenPoolName).getUsed();
                    final long youngAfter = after.get(youngGenPoolName).getUsed();
                    final long delta = youngBefore - youngGenSizeAfter.get();
                    youngGenSizeAfter.set(youngAfter);
                    if (delta > 0L) {
                        allocatedBytes.increment(delta);
                    }
                }
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

    boolean isConcurrentPhase(GarbageCollectionNotificationInfo info) {
        // So far the only indicator known is that the cause will be reported as "No GC"
        // when using CMS.
        //
        // For ZGC, the allocation stall seems to indicate that some thread or threads are
        // blocked trying to allocate. Even though it is not a true STW pause, counting it
        // as such seems to be less confusing.
        return "No GC".equals(info.getGcCause()) // CMS
            || "Shenandoah Cycles".equals(info.getGcName()) // Shenandoah
            || ("ZGC".equals(info.getGcName()) && !"Allocation Stall".equals(info.getGcCause()));
    }

    private static boolean isManagementExtensionsPresent() {
        try {
            Class.forName("com.sun.management.GarbageCollectionNotificationInfo", false,
                JvmGcMetrics.class.getClassLoader());
            return true;
        } catch (Throwable e) {
            // We are operating in a JVM without access to this level of detail
            log.warn("GC notifications will not be available because "
                + "com.sun.management.GarbageCollectionNotificationInfo is not present");
            return false;
        }
    }

    /**
     * Generalization of which parts of the heap are considered "young" or "old" for multiple GC implementations
     */
    @NonNullApi
    enum GcGenerationAge {
        OLD, YOUNG, UNKNOWN;

        private static Map<String, GcGenerationAge> knownCollectors = new HashMap<String, GcGenerationAge>() {
            {
                put("ConcurrentMarkSweep", OLD);
                put("Copy", YOUNG);
                put("G1 Old Generation", OLD);
                put("G1 Young Generation", YOUNG);
                put("MarkSweepCompact", OLD);
                put("PS MarkSweep", OLD);
                put("PS Scavenge", YOUNG);
                put("ParNew", YOUNG);
            }
        };

        static GcGenerationAge fromName(String name) {
            return knownCollectors.getOrDefault(name, UNKNOWN);
        }
    }

}
