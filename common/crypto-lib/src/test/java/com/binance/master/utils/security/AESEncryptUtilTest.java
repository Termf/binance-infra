package com.binance.master.utils.security;

import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

public class AESEncryptUtilTest {

    @Test
    public void encryptWithAES() {
        Set<String> resultSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            byte[] result = AESEncryptUtil.encrypt("hello,world".getBytes(), "passw0rd");
            // System.out.println(i + ":" + Hex.toHexString(result));
            resultSet.add(Hex.toHexString(result));
        }
        Assert.assertEquals(10, resultSet.size());
    }

    @Test
    public void decryptWithAES() {
        final String content = "hello,world";
        byte[] result = AESEncryptUtil.encrypt(content.getBytes(), "passw0rd");
        System.out.println(Hex.toHexString(result));
        byte[] rawData = AESEncryptUtil.decrypt(result, "passw0rd");
        // System.out.println(new String(rawData));
        Assert.assertEquals(content, new String(rawData));

    }

    @Test
    public void hashWithPBKDF21() {
        byte[] result  = HashUtil.hashWithPBKDF2("hello, world", false);
        // System.out.println(Hex.toHexString(result));
        Assert.assertEquals("f530982d490b2d5823c606847c070fe3", Hex.toHexString(result));
    }
}
