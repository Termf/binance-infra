package com.binance.platform.monitor.logging.filter;

import com.binance.platform.monitor.logging.SampleUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/9/27
 */
public class Log4j2SampleFilter extends AbstractFilter {
    private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();

    @Override
    public Result filter(LogEvent event) {
        return filter(event.getLoggerName(), event.getLevel(), event.getContextData());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return filter(logger, level);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return filter(logger, level);
    }

    private Result filter(Logger logger, Level level) {
        ReadOnlyStringMap readOnlyStringMap = currentContextData();
        return filter(logger.getName(), level, readOnlyStringMap);
    }

    private Result filter(String loggerName, Level level, ReadOnlyStringMap readOnlyStringMap) {
        // WARN及以下的日志需要检查采样标记
        if (level.isLessSpecificThan(Level.WARN)
                && !"com.binance.platform.openfeign.body.RequestBodyCacheFilter".equals(loggerName)) {
            Object sampleTagValue = readOnlyStringMap.getValue(SampleUtil.HTTP_HEADER_NAME);
            // 采样结果为不打印并且不是全采样的日志直接拒绝
            if (!SampleUtil.isSampled(sampleTagValue) && !SampleUtil.isFullSample()) {
                return Result.DENY;
            }
        }

        return Result.NEUTRAL;
    }

    private ReadOnlyStringMap currentContextData() {
        return injector.rawContextData();
    }
}
