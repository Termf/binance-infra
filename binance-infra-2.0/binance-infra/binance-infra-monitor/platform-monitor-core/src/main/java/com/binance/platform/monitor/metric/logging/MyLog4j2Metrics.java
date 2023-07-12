package com.binance.platform.monitor.metric.logging;

import static java.util.Collections.emptyList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

public class MyLog4j2Metrics implements MeterBinder {

    private static final String METER_NAME = "log4j2.events";

    private final Iterable<Tag> tags;
    private final LoggerContext loggerContext;

    private MetricsFilter metricsFilter;

    public MyLog4j2Metrics() {
        this(emptyList());
    }

    public MyLog4j2Metrics(Iterable<Tag> tags) {
        this(tags, (LoggerContext)LogManager.getContext(false));
    }

    public MyLog4j2Metrics(Iterable<Tag> tags, LoggerContext loggerContext) {
        this.tags = tags;
        this.loggerContext = loggerContext;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        metricsFilter = new MetricsFilter(registry, tags);
        metricsFilter.start();
        Configuration configuration = loggerContext.getConfiguration();
        configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).addFilter(metricsFilter);
        loggerContext.updateLoggers(configuration);
    }

    class MetricsFilter extends AbstractFilter {

        private final Counter fatalCounter;
        private final Counter errorCounter;
        private final Counter warnCounter;
        private final Counter infoCounter;
        private final Counter debugCounter;
        private final Counter traceCounter;

        MetricsFilter(MeterRegistry registry, Iterable<Tag> tags) {
            fatalCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "fatal")
                .description("Number of fatal level log events").baseUnit("events").register(registry);

            errorCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "error")
                .description("Number of error level log events").baseUnit("events").register(registry);

            warnCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "warn")
                .description("Number of warn level log events").baseUnit("events").register(registry);

            infoCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "info")
                .description("Number of info level log events").baseUnit("events").register(registry);

            debugCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "debug")
                .description("Number of debug level log events").baseUnit("events").register(registry);

            traceCounter = Counter.builder(METER_NAME).tags(tags).tags("level", "trace")
                .description("Number of trace level log events").baseUnit("events").register(registry);
        }

        @Override
        public Result filter(LogEvent event) {
            switch (event.getLevel().getStandardLevel()) {
                case FATAL:
                    fatalCounter.increment();
                    break;
                case ERROR:
                    errorCounter.increment();
                    break;
                case WARN:
                    warnCounter.increment();
                    break;
                case INFO:
                    infoCounter.increment();
                    break;
                case DEBUG:
                    debugCounter.increment();
                    break;
                case TRACE:
                    traceCounter.increment();
                    break;
                default:
                    break;
            }

            return Result.NEUTRAL;
        }
    }
}
