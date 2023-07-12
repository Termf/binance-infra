package com.binance.master.utils.security;

import static com.binance.master.utils.security.common.CryptoConstants.UTF8;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

public final class Base64Util {
    /**
     * base64编码
     *
     * @param data
     * @return
     */
    public static String encode(String data) {
        return encode(data, Charset.forName(UTF8));
    }

    /**
     *
     * @param data
     * @param charset
     * @return
     */
    public static String encode(String data, Charset charset) {
        return Base64.encodeBase64String(data.getBytes(charset));
    }

    /**
     * decode the specified data that with UTF-8 encoding.
     *
     * @param data
     * @return
     */
    public static String decode(String data) {
        return decode(data, Charset.forName(UTF8));
    }


    /**
     *
     * @param data
     * @param charset
     * @return
     */
    public static String decode(String data, Charset charset) {
        return new String(decodeBytes(data.getBytes(charset)), charset);
    }

    /**
     * base64解码.
     *
     * @param rawData
     * @return
     */
    public static byte[] decodeBytes(byte[] rawData) {
        return Base64.decodeBase64(rawData);
    }

}
