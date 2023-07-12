package com.binance.master.utils;

import java.math.BigDecimal;

/**
 * 撮合计算
 * 
 * @author wang
 *
 */
@Deprecated
public class CouplingCalculationUtils {

    private static final BigDecimal FEE_POINT = new BigDecimal("10000");

    public static long feeLong(final BigDecimal commission) {
        return FEE_POINT.multiply(commission).longValue();
    }
}
