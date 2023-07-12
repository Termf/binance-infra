package com.binance.mbx.sample.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/1/6 12:03 下午
 */
public class SignatureUtil {
    /**
     * {
     *     "apiKey": "4Vrlx8fOgfInmZR1Rt6WKktDLTZohXoTAUB4DrZ0k7Eh45uNbOacXa7IpnWmh32j",
     *     "secretKey": "lf1V8BdXSmz1q7TNXWN4prxyhYjwcbw874Jy4rCeI1qqd72E4ezhxuEHMLGscYTL",
     *     "keyId": 341,
     *     "accountId": 2172,
     *     "type": "HMAC_SHA256"
     * }
     */

    public static String sha256_HMAC(String key, String data) {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            mac.init(secret_key);
            return Hex.encodeHexString(mac.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static long getTime() {
        return System.currentTimeMillis();
    }
}
