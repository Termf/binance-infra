package com.binance.platform.mbx.util;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/18 10:54 上午
 */
public class ArrayValueUtil {
    public static final String RESULT_PREFIX = "[";
    public static final String RESULT_DELIMITER = ",";
    public static final String RESULT_SUFFIX = "]";
    public static final String EMPTY_RESULT = RESULT_PREFIX + RESULT_SUFFIX;

    /**
     * Convert a list to a String
     *
     * @param list
     * @return
     */
    public static String convert(List<String> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return EMPTY_RESULT;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RESULT_PREFIX);
        boolean first = true;
        for (String value : list) {
            if (!first) {
                stringBuilder.append(RESULT_DELIMITER);
            } else {
                first = false;
            }
            stringBuilder.append("\"").append(value).append("\"");
        }
        stringBuilder.append(RESULT_SUFFIX);
        return stringBuilder.toString();
    }

}
