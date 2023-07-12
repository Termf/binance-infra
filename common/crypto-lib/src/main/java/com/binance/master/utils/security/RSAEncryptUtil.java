package com.binance.master.utils.security;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSAEncryptUtil {

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 服务器端RSA私钥加密
     *
     * @param content 加密字符串
     * @param privateKeyStr 私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String content, String privateKeyStr) throws Exception {
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyStr);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes("UTF-8")));
    }

    /**
     * 客户端RSA公钥解密
     *
     * @param content 加密字符串
     * @param publicKeyStr 公钥
     * @returnm 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String content, String publicKeyStr) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(content.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKeyStr);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return new String(cipher.doFinal(inputByte));
    }



    /**
     * 客户端RSA公钥加密
     *
     * @param content 加密字符串
     * @param publicKeyStr 私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptForClient(String content, String publicKeyStr) throws Exception {
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKeyStr);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] inputArray = content.getBytes("UTF-8");
        int inputLength = inputArray.length;
        int offSet = 0;
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while (inputLength - offSet > 0) {
            byte[] cache;
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            bops.write(cache);
        }
        bops.close();
        return Base64.encodeBase64String(bops.toByteArray());
    }

    /**
     * 服务器端RSA私钥解密
     *
     * @param content 加密字符串
     * @param privateKeyStr 私钥
     * @returnm 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decryptForServer(String content, String privateKeyStr) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(content.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKeyStr);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // 标识
        int inputLength = inputByte.length;
        int offSet = 0;
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        while (inputLength - offSet > 0) {
            byte[] cache;
            if (inputLength - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(inputByte, offSet, MAX_DECRYPT_BLOCK);
                offSet += MAX_DECRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputByte, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            bops.write(cache);
        }
        return new String(bops.toByteArray());
    }

}
