package com.binance.master.utils;

/**
 * @author: caixinning
 * @date: 2018/06/22 16:02
 **/
public class NumberUtils {

    /**
     * force to Integer（WARNING: may involve rounding or truncation）
     */
    public static Integer forceInteger(Object num) {
        if (num == null) {
            return null;
        }
        if (num instanceof String) {
            return Integer.valueOf((String) num);
        }
        if (num instanceof Number) {
            return ((Number) num).intValue();
        }
        return (Integer) num;
    }

}
