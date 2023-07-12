package com.binance.master.utils.security.asymmetric.encryption;

import com.binance.master.utils.security.asymmetric.IDecryptor;
import com.binance.master.utils.security.common.EncryptionException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import static com.binance.master.utils.security.common.CryptoConstants.KEY_RSA;

public class DecryptorImpl implements IDecryptor {

    private final KeyFactory keyFactory;
    private final Key privateK;

    // RSA最大解密密文大小
    private static final int RSA_MAX_DECRYPT_BLOCK = 128;

    /**
     * 私钥(BASE64编码).
     * PKCS#8格式，去除第一行的开头和最后一行的结束，即<<BASE64 ENCODED DATA>>部分。
     *
     * -----BEGIN PUBLIC KEY-----
     * <<BASE64 ENCODED DATA>>
     * -----END PUBLIC KEY-----
     * 
     * @param privateKey
     */
    public DecryptorImpl(String privateKey) {
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            keyFactory = KeyFactory.getInstance(KEY_RSA);
            privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new EncryptionException("", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedData) throws EncryptionException {
        try {
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
        } catch (Exception e) {
            throw new EncryptionException("failed to decrypt the encrypted data", e);
        }

    }
}
