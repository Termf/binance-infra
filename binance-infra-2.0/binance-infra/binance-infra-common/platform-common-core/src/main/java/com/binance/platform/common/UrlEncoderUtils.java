package com.binance.platform.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class UrlEncoderUtils {

    private static BitSet dontNeedEncoding;

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set('+');
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    public static boolean hasUrlEncoded(String str) {
        boolean needEncode = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (dontNeedEncoding.get((int)c)) {
                continue;
            }
            if (c == '%' && (i + 2) < str.length()) {
                // 判断是否符合urlEncode规范
                char c1 = str.charAt(++i);
                char c2 = str.charAt(++i);
                if (isDigit16Char(c1) && isDigit16Char(c2)) {
                    continue;
                }
            }
            // 其他字符，肯定需要urlEncode
            needEncode = true;
            break;
        }
        if (StringUtils.startsWithAny(str, "+", "*")) {
            needEncode = true;
        }
        return !needEncode;
    }

    public static boolean hasUrlEncoded(Collection<String> str) {
        for (String s : str) {
            if (hasUrlEncoded(s)) {
                return true;
            }
        }
        return false;

    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name()).replace("+", "%20").replace("*", "%2A")
                .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }

    private static boolean isDigit16Char(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }

    public static void main(String[] args) {
        String test = "+86187";
        System.out.print(hasUrlEncoded(urlEncode(test)));
    }
}