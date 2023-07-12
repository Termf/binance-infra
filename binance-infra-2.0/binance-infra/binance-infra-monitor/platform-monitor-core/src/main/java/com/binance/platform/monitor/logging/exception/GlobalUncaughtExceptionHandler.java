package com.binance.platform.monitor.logging.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        String message = sw.getBuffer().toString();
        logger.warn("An exception has been raised by Name:{},ThreadId:{},Message:{}", t.getName(), t.getId(), message);
    }

}
