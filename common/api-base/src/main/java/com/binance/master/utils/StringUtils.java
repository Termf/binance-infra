package com.binance.master.utils;


import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;


public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String BASE_RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String FULL_RANDOM_CHARS = "1234567890ABCDEFGHIJKLMNIOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_RANDOM_CHARS = "0123456789";
    private static SecureRandom secureRandom = new SecureRandom();

    public static String uuid() {
        return RegExUtils.removeAll(UUID.randomUUID().toString(), "-");
    }


    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int size = BASE_RANDOM_CHARS.length();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(BASE_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    public static String getNumberRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        int size = NUMBER_RANDOM_CHARS.length();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(NUMBER_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    public static String objectToString(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static String nullToEmpty(Object s) {
        return s != null ? s.toString().trim() : "";
    }

    public static String getMobileKey(String mobile, String country) {
        StringBuffer sb = new StringBuffer();
        sb.append(country);
        sb.append(":");
        sb.append(mobile);
        return sb.toString();
    }

    /**
     * 获取32位长度的随机串，前13位为时间戳
     */
    public static String getTimestampRandom32() {
        StringBuilder sb = new StringBuilder().append(System.currentTimeMillis());
        int size = FULL_RANDOM_CHARS.length();
        for (int i = 0; i < 19; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(FULL_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机串
     *
     * @param length 长度
     */
    public static String getRandom(int length) {
        Assert.isTrue(length > 0, String.format("length必须为正整数: %s", length));
        StringBuilder sb = new StringBuilder();
        int size = FULL_RANDOM_CHARS.length();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(size);
            sb.append(FULL_RANDOM_CHARS.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 拼接url参数，过滤空的key和vaule
     *
     * @param params map参数
     * @return ?key1=val1&key2=val2
     */
    public static String buildUrlParam(Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("?");
        params.forEach((k, v) -> {
            if (isNotBlank(k) && v != null && isNotBlank(v.toString())) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        // 若只有'?', 则返回空字符串
        if (sb.length() == 1) {
            return "";
        }
        // 截掉最后一个'&'
        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换为Byte[] （pnk-core方法迁移）
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将Byte[] 转换成16进制字符串 （pnk-core方法迁移）
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
//
//    public static void main(String[] args) {
//        System.out.println(getTimestampRandom32());
//        System.out.println(getTimestampRandom32());
//        System.out.println(getTimestampRandom32());
//        System.out.println(getTimestampRandom32());
//        System.out.println(getTimestampRandom32());
//        System.out.println(getTimestampRandom32().length());
//        Map<String, String> map = new HashMap<>();
//        map.put("a", null);
//        map.put("b", "");
//        map.put("", "122");
//        map.put("c", "122");
//        System.out.println(buildUrlParam(map));
//    }

}
