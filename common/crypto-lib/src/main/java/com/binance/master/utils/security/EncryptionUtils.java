package com.binance.master.utils.security;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.binance.master.utils.security.common.RSAKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 加密类
 * 
 * @author wang-bijie
 *
 */
@Deprecated
public final class EncryptionUtils {
    private final static String CHARSET = "UTF-8";
    // 加密算法SHA
    private final static String KEY_SHA = "SHA";
    // 加密算法RSA
    public static final String KEY_RSA = "RSA";
    // 加密算法AES
    public static final String KEY_AES = "AES";
    // 签名算法
    public static final String SIGNATURE_MD5_WITH_RSA = "MD5withRSA";
    // RSA最大加密明文大小
    private static final int RSA_MAX_ENCRYPT_BLOCK = 117;
    // RSA最大解密密文大小
    private static final int RSA_MAX_DECRYPT_BLOCK = 128;

    private static final Pattern PTN_BASE64 =
            Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");

    /**
     * base64加密
     * 
     * @param data
     * @return
     */
    public static String base64Encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

    public static String base64Encode(String data, Charset charset) {
        return Base64.encodeBase64String(data.getBytes(charset));
    }

    public static String base64DecodeToString(String data) {
        return new String(base64Decode(data));
    }

    public static String base64DecodeToString(String data, Charset charset) {
        return new String(base64Decode(data), charset);
    }

    /**
     * base64解密
     * 
     * @param data
     * @return
     */
    public static byte[] base64Decode(String data) {
        return Base64.decodeBase64(data.getBytes());
    }

    /**
     * md5加密.
     *
     * deprecate this api because it's not safe enough.
     * 
     * @param data
     * @return
     */
    @Deprecated
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 
     * @param data
     * @return
     */
    public static String sha256Hex(String data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * sha 加密
     * 
     * @param data 原数据
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha(String data) throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return Hex.encodeHexString(bytes);
    }

    /**
     * 生成密钥对(公钥和私钥)
     * 
     * @return
     * @throws Exception
     */
    public static RSAKey genKeyPair() throws Exception {
        return RSAKeyUtil.genKeyPair();
    }

    /**
     * 用私钥对信息生成数字签名
     * 
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_MD5_WITH_RSA);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 校验数字签名
     * 
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_MD5_WITH_RSA);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 私钥解密
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > RSA_MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, RSA_MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * RSA_MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥解密
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > RSA_MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, RSA_MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * RSA_MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     * 
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > RSA_MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, RSA_MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * RSA_MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥加密
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > RSA_MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, RSA_MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * RSA_MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    private static SecretKeySpec initKeyForAES(String key) throws NoSuchAlgorithmException {
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec secretKeySpec = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            secretKeySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return secretKeySpec;
    }

    /**
     * 加密
     * 
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encryptAES(String content, String password){
        try {
            return encryptAES(content.getBytes(CHARSET), password);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encryptAES error", e);
        }
    }

    /**
     * 加密：
     *  1.创建并初始化密码器
     *  2.加密数据
     *  Note：该方法已过时，请使用AESEncryptUtil.encrypt(...)进行加密。
     */
    @Deprecated
    public static byte[] encryptAES(byte[] content, String password){
        try {
            SecretKeySpec key = initKeyForAES(password);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException("encryptAES error", e);
        }
    }

    @Deprecated
    public static String encryptAESToString(String content, String password) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        return Base64.encodeBase64String(encryptAES(content, password));
    }

    /**
     * 解密.
     *  1.创建并初始化密码器
     *  2.解密数据
     *  Note：该方法已过时，请使用AESEncryptUtil.decrypt(...)进行解密。
     * @param content 待解密内容
     * @param password 解密密钥
     * @return
     */
    @Deprecated
    public static byte[] decryptAES(byte[] content, String password){
        try {
            SecretKeySpec key = initKeyForAES(password);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException("decryptAES error", e);
        }
    }

    @Deprecated
    public static String decryptAESToString(String content, String password) {
        return new String(EncryptionUtils.decryptAES(Base64.decodeBase64(content), password));
    }

    public static String encryptAES(String content, String password1, String password2) {
        final byte[] encAes = EncryptionUtils.encryptAES(content, password1);
        final String encAesStr = Base64.encodeBase64String(encAes);
        byte[] encAes2 = EncryptionUtils.encryptAES(encAesStr, password2);
        return Base64.encodeBase64String(encAes2);
    }

    public static String decryptAES(String content, String password1, String password2) {
        String decAesStr2 = new String(EncryptionUtils.decryptAES(Base64.decodeBase64(content), password2));
        return new String(EncryptionUtils.decryptAES(Base64.decodeBase64(decAesStr2), password1));
    }

    /**
     * 判断字符串是否经过base64编码
     * 
     * @param str
     * @return true or false
     */
    public static boolean isBase64(String str) {
        if (str == null) {
            return false;
        }
        return PTN_BASE64.matcher(str).matches();
    }

    /**
     * HmacSHA256 加密， 返回结果以base64处理
     */
    public static String hmacSha256(String data, String secret, Charset charset){
        return HashUtil.hmacSha256(data, secret, charset);
    }

    /**
     * 默认使用 ISO_8859_1编码格式
     */
    public static String hmacSha256(String data, String secret){
        return HashUtil.hmacSha256(data, secret);
    }
}
