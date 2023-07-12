package com.binance.master.utils.security;

import com.binance.master.utils.security.asymmetric.CryptoFactory;
import com.binance.master.utils.security.asymmetric.IEncryptor;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EncryptionUtilsTest {

    @Test
    public void safeEncryptAES() {
        byte[] result = EncryptionUtils.encryptAES("hello,world".getBytes(), "safe-key");
        assertNotNull(result);
        System.out.println(Hex.encodeHex(result));
    }

    @Test
    public void safeDecryptAES() {
    }

    @Test
    public void encryptByPublicKey() {
        String pubKey = //"-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA7yFQFlLO8zRQABWjyKzq\n" +
                "JueP1jLD3oOcvubzIoRgDHgp3/+uWA1VjjFRXUk1cqNNUG8NsmjQNenTZZYcc36P\n" +
                "DMZm4d5lJpYU0sVudV4PaboZ0yL0WXgLbi8+dFfoCL/PsRNovXUes7IaNwMVq3IM\n" +
                "Y8bXnCtERdi4BEN4+0CUb6BdGG3Cda1yW/p/Rf3595x3sRbjoS+0vXXUdt0x0PNH\n" +
                "AO2/syKTFzuEwRog7bsBFAatHOq1j4GeUotdqAX8fI5QlPfkoRjrNtfqc2TXlCoO\n" +
                "22XW6MPTRY1kfgu1Nfb2GHmcqG8+K5gLdYjEjhQuwY9x5voHXh9VSP+s2QxaqMxx\n" +
                "Q7W9OQrYEKXQCnhJuWrRWxGZj/HHMaxHO8yHi+RlD/vrNZC5jOyiwTm58FU+hvbx\n" +
                "+QV6NCGjLFNSL/L63PgwlEBR9FRXz0P6oDMH5ZAokK7J2mKNt0CRr/NHCflGXAU8\n" +
                "dGVcVfd1yec4eCO5bdlz/c57ohLMVHjv/+LYUPJstJYAi9U8OOwrlQ62/xzn71l0\n" +
                "QDOub8hvmSLmJI7h0IBPJSaugUnAtQ01NPD8hF3zkvzv2vjrthCgW3fctqnTLKm3\n" +
                "Ca/L92NF5m71DFNh/snnljF+SFx2JpJ8txD7XxlzGoX30rpE9f4GDLPWCT8x6GlG\n" +
                "yNMvW33w2Kd9PAJru8t39ckCAwEAAQ==\n";
                //"-----END PUBLIC KEY-----";
        try {
             IEncryptor encryptor = CryptoFactory.createEncryptor(pubKey);
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {

                byte[] data = "hello,world".getBytes();
//                byte[] result = EncryptionUtils.encryptByPublicKey(data, pubKey);
//                Assert.assertEquals(512, result.length);


                byte[] result2 = encryptor.encrypt(data);
                Assert.assertEquals(512, result2.length);
                // result2 = EncryptionUtils.encryptByPublicKey(data, pubKey);
                //
                // Assert.assertArrayEquals(result, result2);
                //System.out.println(Hex.encodeHexString(result));
                // System.out.println(Hex.encodeHexString(result2));
            }
            long end = System.currentTimeMillis();
            System.out.println("duration "+(end-start));
        } catch (Exception e) {
        }
    }
}