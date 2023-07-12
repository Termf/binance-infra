package com.binance.platform.monitor.logging.appender.log4j.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
import org.apache.logging.log4j.core.async.EventRoute;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 队列满时，高于阈值的日志将直接输出到Appender，不再进行入队动作，避免对业务线程造成阻塞。
 *
 * @author Thomas Li
 * Date: 2021/9/19
 */
public class DiscardAsyncQueueFullPolicy implements AsyncQueueFullPolicy {
    private static final Logger LOGGER = StatusLogger.getLogger();

    static final String PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL = "log4j2.DiscardThreshold";
    private final Level thresholdLevel;
    private final AtomicLong discardCount = new AtomicLong();

    public DiscardAsyncQueueFullPolicy() {
        final PropertiesUtil util = PropertiesUtil.getProperties();
        final String level = util.getStringProperty(PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL, Level.INFO.name());
        final Level thresholdLevel = Level.toLevel(level, Level.INFO);
        LOGGER.debug("Creating custom BinanceAsyncQueueFullPolicy(discardThreshold:{})", thresholdLevel);
        this.thresholdLevel = thresholdLevel;
    }

    @Override
    public EventRoute getRoute(long backgroundThreadId, Level level) {
        if (level.isLessSpecificThan(thresholdLevel)) {
            if (discardCount.getAndIncrement() == 0) {
                LOGGER.warn("Async queue is full, discarding event with level {}. " +
                                "This message will only appear once; future events from {} " +
                                "are silently discarded until queue capacity becomes available.",
                        level, thresholdLevel);
            }
            return EventRoute.DISCARD;
        }
        return EventRoute.SYNCHRONOUS;
    }
}
