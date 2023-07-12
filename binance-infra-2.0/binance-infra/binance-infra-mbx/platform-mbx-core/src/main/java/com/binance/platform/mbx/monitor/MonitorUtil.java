package com.binance.platform.mbx.monitor;

import com.binance.platform.mbx.exception.MbxException;

/**
 * @author Li Fenggang
 * Date: 2020/7/9 6:52 上午
 */
public class MonitorUtil {
    public static final String METRIC_VALUE_NONE = "None";

    public static void entrance(String entrance) {
        MonitorContext monitorContext = MonitorContextHolder.get();
        if (monitorContext != null) {
            monitorContext.setEntrance(entrance);
        }
    }

    public static String entrance() {
        MonitorContext monitorContext = MonitorContextHolder.get();
        return monitorContext.getEntrance();
    }

    public static String getExceptionTag(Exception ex) {
        if (ex == null) {
            return METRIC_VALUE_NONE;
        }
        Class<?> objectClass = ex.getClass();
        StringBuilder stringBuilder = new StringBuilder(objectClass.getSimpleName());
        if (ex instanceof MbxException) {
            MbxException mbxEx = (MbxException)ex;
            stringBuilder.append("-").append(mbxEx.getErrorCode());
        }

        return stringBuilder.toString();
    }

    public static String nonNullProcess(Object rawValue) {
        if (rawValue == null) {
            return METRIC_VALUE_NONE;
        }
        return rawValue.toString();
    }
}
