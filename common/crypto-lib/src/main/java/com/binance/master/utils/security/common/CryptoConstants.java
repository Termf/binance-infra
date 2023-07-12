package com.binance.master.utils.security.common;

public final class CryptoConstants {
    /**
     * size of the secret key: 16 bytes.
     */
    public static final int SECRET_KEY_BYTES = 16;
    public static final int ITERATIONS = 512;

    public static final String AES = "AES";
    public static final String AES_GCM_PKCS_5_PADDING = "AES/GCM/PKCS5Padding";
    public static final String PBKDF_2_WITH_HMAC_SHA_256 = "PBKDF2WithHmacSHA256";

    public final static String UTF8 = "UTF-8";

    // 加密算法RSA
    public static final String KEY_RSA = "RSA";
}
