package com.binance.master.log.layout;

/**
 * used for BinancePatternLayout.
 * @author james.li
 */
public class LayoutUtil {

    /**
     * escape the srcValue to the destBuf.
     *
     * @param srcValue
     * @return
     */
    public static String escape(StringBuilder srcValue) {
        StringBuilder destBuf = new StringBuilder();
        for (int i = 0; i < srcValue.length(); i++) {
            char c = srcValue.charAt(i);
            switch (c) {
                case '\"':
                case '\\':
                case '/':
                    // escape special symbols in the srcValue
                    destBuf.append("\\");
                    destBuf.append(c);
                    break;
                case '\b':
                    destBuf.append("\\b");
                    break;
                case '\f':
                    destBuf.append("\\f");
                    break;
                case '\n':
                    destBuf.append("\\n");
                    break;
                case '\r':
                    destBuf.append("\\r");
                    break;
                case '\t':
                    destBuf.append("\\t");
                    break;
                default:
                    destBuf.append(c);
                    break;
            }
        }
        return destBuf.toString();
    }

    /**
     * extract the ip information for snippet of log.
     *
     * @param ipBuf
     */
    public static String extractIP(StringBuilder ipBuf) {
        StringBuilder destBuf = new StringBuilder();
        boolean foundStart = false;
        int dots = 0;
        for (int i = 0; i < ipBuf.length(); i++) {
            char c = ipBuf.charAt(i);
            if (c >= '0' && c <= '9') {
                if (foundStart == false) {
                    foundStart = true;
                }
                destBuf.append(c);
            } else if (foundStart && c == '.') {
                dots++;
                destBuf.append(c);
            } else if (foundStart && dots == 3) {
                break;
            }
        };
        return destBuf.toString();
    }

    /**
     * build one json item for the specified key and value.
     * @param key
     * @param value
     * @param destBuf
     */
    public static void buildJsonItem(String key, String value, StringBuilder destBuf) {
        destBuf.append("\"");
        destBuf.append(key);
        destBuf.append("\":\"");
        destBuf.append(value);
        destBuf.append("\",");
    }
}
