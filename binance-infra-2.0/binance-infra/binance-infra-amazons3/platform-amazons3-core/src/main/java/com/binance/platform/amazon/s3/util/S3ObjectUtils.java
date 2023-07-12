package com.binance.platform.amazon.s3.util;

public class S3ObjectUtils {



    /**
     *
     * 把开头的 / 去掉
     *
     * @param objKey
     * @return
     */
    public static String escapeObjectKey(String objKey) {
        return objKey.replaceAll("^/+", "");
    }

    /**
     * objKey to externalImageId
     * @param objKey
     * @return
     */
    public static String objectKeyToExternalImageId(String objKey) {
        objKey = escapeObjectKey(objKey);
        return objKey.replaceAll("[^a-zA-Z0-9_.\\-:]", "_");
    }

}
