package com.binance.master.utils.security;

import static com.binance.master.utils.security.AESEncryptUtil.genRandomSalt;
import static com.binance.master.utils.security.common.CryptoConstants.*;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.binance.master.utils.security.common.EncryptionException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public final class HashUtil {
    // 哈希算法SHA
    private final static String KEY_SHA = "SHA";

    /**
     *
     * @param content
     * @param salt
     * @return
     * @throws EncryptionException
     */
    public static byte[] hashWithPBKDF2(String content, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(content.toCharArray(), salt, ITERATIONS, SECRET_KEY_BYTES * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256);
            return factory.generateSecret(spec).getEncoded();
        }catch (Exception e){
            throw new EncryptionException("hash exception occur for algorithm PBKDF2WithHmacSHA256", e);
        }
    }

    /**
     *
     * @param content
     * @param randomSalt
     * @return
     */
    public static byte[] hashWithPBKDF2(String content, boolean randomSalt) {
        byte[] salt;
        if (randomSalt) {
            salt = genRandomSalt();
        } else {
            salt = new byte[SECRET_KEY_BYTES];
        }
        return hashWithPBKDF2(content, salt);
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
    @Deprecated
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
     * HmacSHA256 加密， 返回结果以base64处理
     */
    public static String hmacSha256(String data, String secret, Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset cannot be null");
        }
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(charset), "HmacSHA256");
            sha256HMAC.init(secretKey);
            return Base64.encodeBase64String(sha256HMAC.doFinal(data.getBytes(charset)));
        } catch (Exception e) {
            throw new RuntimeException("hmacSha256 exception, data:" + data, e);
        }
    }

    /**
     * 采用HmacSHA256算法对数据进行hash.
     *
     * @param data 待哈希的数据，默认使用 ISO_8859_1编码格式
     * @param secret 哈希算法使用的密钥，默认使用 ISO_8859_1编码格式
     * @return
     */
    public static String hmacSha256(String data, String secret) {
        return hmacSha256(data, secret, Charset.forName("ISO-8859-1"));
    }
}
