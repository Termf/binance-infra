package com.binance.master.utils.security.asymmetric.encryption;

import static com.binance.master.utils.security.common.CryptoConstants.KEY_RSA;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.binance.master.utils.security.asymmetric.IEncryptor;
import com.binance.master.utils.security.common.EncryptionException;

public class EncryptorImpl implements IEncryptor {

    // RSA最大加密明文大小
    private static final int RSA_MAX_ENCRYPT_BLOCK = 117;

    private final KeyFactory keyFactory;
    private final Key publicK;

    /**
     * 公钥(BASE64编码).
     *
     * PKCS#8格式，去除第一行的开头和最后一行的结束，即<<BASE64 ENCODED DATA>>部分。
     * -----BEGIN PUBLIC KEY-----
     * <<BASE64 ENCODED DATA>>
     * -----END PUBLIC KEY-----
     * @param publicKey
     */
    public EncryptorImpl(String publicKey) {
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            keyFactory = KeyFactory.getInstance(KEY_RSA);
            publicK = keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            throw new EncryptionException("failed to load init encryptor with the public key", e);
        }
    }

    /**
     * 公钥加密
     *
     * @param data 源数据
     * @return
     */
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
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
        } catch (Exception e) {
            throw new EncryptionException("failed to encrypt data with public key", e);
        }
    }
}
