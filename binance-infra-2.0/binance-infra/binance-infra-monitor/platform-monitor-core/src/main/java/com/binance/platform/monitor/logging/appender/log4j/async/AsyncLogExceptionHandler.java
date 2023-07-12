package com.binance.platform.monitor.logging.appender.log4j.async;

import org.apache.logging.log4j.core.async.AsyncLoggerDefaultExceptionHandler;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步日志错误处理器
 *
 * @author Thomas Li
 * Date: 2021/9/13
 */
public class AsyncLogExceptionHandler extends AsyncLoggerDefaultExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AsyncLogExceptionHandler.class);

    @Override
    public void handleEventException(Throwable throwable, long sequence, RingBufferLogEvent event) {
        StringBuilder eventStringBuilder = new StringBuilder();
        try {
            eventStringBuilder.append(event.toString());
        } catch (Throwable t) {
            eventStringBuilder.append("ERROR calling toString() on ").append(event.getClass().getName())
                    .append(": ").append(t.toString());
        }
        log.warn("AsyncLogger error handling event seq={}, value={}", sequence, eventStringBuilder.toString(), throwable);
    }
}
