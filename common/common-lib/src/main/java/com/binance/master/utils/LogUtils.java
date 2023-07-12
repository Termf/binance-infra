package com.binance.master.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.util.StackLocator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LogUtils {

    private static final Map<String, Logger> cache = new ConcurrentHashMap<>();
    private static final String className = LogUtils.class.getName();

    public static Logger getLogger(final Class<?> clazz) {
        return (Logger) LogManager.getLogger(LogUtils.class);
    }

    public static void error(Object message) {
        getLogger().logIfEnabled(className, Level.ERROR, null, message, null);
    }

    public static void error(String message, Object... params) {
        getLogger().logIfEnabled(className, Level.ERROR, null, message, params);
    }

    public static void error(Object message, Throwable e) {
        getLogger().logIfEnabled(className, Level.ERROR, null, message, e);
    }

    public static void debug(Object message) {
        getLogger().logIfEnabled(className, Level.DEBUG, null, message, null);
    }

    public static void debug(String message, Object... params) {
        getLogger().logIfEnabled(className, Level.DEBUG, null, message, params);
    }

    public static void debug(Object message, Throwable e) {
        getLogger().logIfEnabled(className, Level.DEBUG, null, message, e);
    }

    public static void info(Object message) {
        getLogger().logIfEnabled(LogUtils.class.getName(), Level.INFO, null, message, null);
    }

    public static void info(String message, Object... params) {
        getLogger().logIfEnabled(LogUtils.class.getName(), Level.INFO, null, message, params);
    }

    public static void info(Object message, Throwable e) {
        getLogger().logIfEnabled(LogUtils.class.getName(), Level.INFO, null, message, e);
    }

    public static void fatal(Object message) {
        getLogger().logIfEnabled(className, Level.FATAL, null, message, null);
    }

    public static void fatal(String message, Object... params) {
        getLogger().logIfEnabled(className, Level.FATAL, null, message, params);
    }

    public static void fatal(Object message, Throwable e) {
        getLogger().logIfEnabled(className, Level.FATAL, null, message, e);
    }

    public static void warn(Object message) {
        getLogger().logIfEnabled(className, Level.WARN, null, message, null);
    }

    public static void warn(String message, Object... params) {
        getLogger().logIfEnabled(className, Level.WARN, null, message, params);
    }

    public static void warn(Object message, Throwable e) {
        getLogger().logIfEnabled(className, Level.WARN, null, message, e);
    }

    public static Logger getLogger() {
        String clazzName = StackLocator.getInstance().getCallerClass(3).getName();
        Logger logger = cache.get(clazzName);
        if (logger == null) {
            logger = (Logger) LogManager.getLogger(clazzName);
            cache.put(clazzName, logger);
        }
        return logger;
    }
}
