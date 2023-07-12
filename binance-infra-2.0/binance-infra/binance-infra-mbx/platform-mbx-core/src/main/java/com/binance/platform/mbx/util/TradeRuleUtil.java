package com.binance.platform.mbx.util;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 10:39 上午
 */
public class TradeRuleUtil {
    public static int formatFeeToInt(BigDecimal source) {
        BigDecimal factor = new BigDecimal("10000");
        int value = source.multiply(factor).intValue();
        return value;
    }

    public static BigDecimal formatFeeToBigDecimal(BigDecimal source) {
        int intValue = formatFeeToInt(source);
        return new BigDecimal(intValue);
    }
}
