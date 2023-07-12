package com.binance.platform.mbx.util;

import java.util.Date;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 10:37 下午
 */
public class DateUtil {

    /**
     * Return null if the date is null, or return the result of its getTime().
     *
     * @param date
     * @return
     */
    public static Long getTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime();
    }
}
