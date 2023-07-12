package com.binance.platform.monitor.metric.logging;

import static java.util.Collections.emptyList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class MyLogbackMetrics implements MeterBinder {
    static ThreadLocal<Boolean> ignoreMetrics = new ThreadLocal<>();

    private final Iterable<Tag> tags;
    private final LoggerContext loggerContext;
    private final Map<MeterRegistry, MetricsTurboFilter> metricsTurboFilters = new ConcurrentHashMap<>();

    public MyLogbackMetrics() {
        this(emptyList());
    }

    public MyLogbackMetrics(Iterable<Tag> tags) {
        this(tags, (LoggerContext)LoggerFactory.getILoggerFactory());
    }

    public MyLogbackMetrics(Iterable<Tag> tags, LoggerContext context) {
        this.tags = tags;
        this.loggerContext = context;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        MetricsTurboFilter filter = new MetricsTurboFilter(registry, tags);
        metricsTurboFilters.put(registry, filter);
        loggerContext.addTurboFilter(filter);
    }

    public static void ignoreMetrics(Runnable r) {
        ignoreMetrics.set(true);
        r.run();
        ignoreMetrics.remove();
    }

}

class MetricsTurboFilter extends TurboFilter {
    private final Counter errorCounter;
    private final Counter warnCounter;
    private final Counter infoCounter;
    private final Counter debugCounter;
    private final Counter traceCounter;

    MetricsTurboFilter(MeterRegistry registry, Iterable<Tag> tags) {
        errorCounter = Counter.builder("logback.events").tags(tags).tags("level", "error")
            .description("Number of error level events that made it to the logs").baseUnit("events").register(registry);

        warnCounter = Counter.builder("logback.events").tags(tags).tags("level", "warn")
            .description("Number of warn level events that made it to the logs").baseUnit("events").register(registry);

        infoCounter = Counter.builder("logback.events").tags(tags).tags("level", "info")
            .description("Number of info level events that made it to the logs").baseUnit("events").register(registry);

        debugCounter = Counter.builder("logback.events").tags(tags).tags("level", "debug")
            .description("Number of debug level events that made it to the logs").baseUnit("events").register(registry);

        traceCounter = Counter.builder("logback.events").tags(tags).tags("level", "trace")
            .description("Number of trace level events that made it to the logs").baseUnit("events").register(registry);
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        Boolean ignored = MyLogbackMetrics.ignoreMetrics.get();
        if (ignored != null && ignored) {
            return FilterReply.NEUTRAL;
        }

        // cannot use logger.isEnabledFor(level), as it would cause a StackOverflowError by calling this filter again!
        if (level.isGreaterOrEqual(logger.getEffectiveLevel()) && format != null) {
            switch (level.toInt()) {
                case Level.ERROR_INT:
                    errorCounter.increment();
                    break;
                case Level.WARN_INT:
                    warnCounter.increment();
                    break;
                case Level.INFO_INT:
                    infoCounter.increment();
                    break;
                case Level.DEBUG_INT:
                    debugCounter.increment();
                    break;
                case Level.TRACE_INT:
                    traceCounter.increment();
                    break;
            }
        }

        return FilterReply.NEUTRAL;
    }
}
