package com.binance.master.utils.security;

import static com.binance.master.utils.security.HashUtil.hashWithPBKDF2;
import static com.binance.master.utils.security.common.CryptoConstants.*;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.binance.master.utils.security.common.EncryptionException;

public class AESEncryptUtil {


    /**
     * generate random salt value.
     *
     * @return
     */
    static byte[] genRandomSalt() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[SECRET_KEY_BYTES];
            random.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            throw new EncryptionException("no SHA1PRNG algorithm was found", e);
        }
    }

    /**
     * generate an secret key spec according to the specified <code>password</code> and
     * <code>salt</code>.
     * 
     * @param password
     * @param salt
     * @return
     */
    private static SecretKeySpec genKey(String password, byte[] salt) {
        byte[] bytes = hashWithPBKDF2(password, salt);
        return new SecretKeySpec(bytes, AES);
    }


    /**
     * encrypt the <code>content</code> with the <code>password</code> and AES/CBC/PKCS5Padding
     * algorithm.
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] encrypt(byte[] content, String password) {
        try {
            // generate random salt and secret key
            byte[] salt = genRandomSalt();
            SecretKeySpec key = genKey(password, salt);
            // initial iv parameter
            AlgorithmParameters params = AlgorithmParameters.getInstance(AES);
            params.init(new IvParameterSpec(new byte[SECRET_KEY_BYTES]));
            // encryption
            Cipher cipher = Cipher.getInstance(AES_GCM_PKCS_5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, params);
            byte[] encryptedData = cipher.doFinal(content);
            byte[] result = new byte[SECRET_KEY_BYTES + encryptedData.length];
            // insert the salt into the head of the result
            System.arraycopy(salt, 0, result, 0, salt.length);
            System.arraycopy(encryptedData, 0, result, salt.length, encryptedData.length);
            return result;
        } catch (Exception e) {
            throw new EncryptionException("encryption error", e);
        }
    }

    /**
     * decrypt the content with the specified password and AES/CBC/PKCS5Padding algorithm.
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        if (content == null || content.length <= SECRET_KEY_BYTES) {
            throw new RuntimeException("invalid content for decryption");
        }
        try {
            // extract the salt and encrypted data
            byte[] salt = new byte[SECRET_KEY_BYTES];
            byte[] encryptedData = new byte[content.length - SECRET_KEY_BYTES];
            System.arraycopy(content, 0, salt, 0, SECRET_KEY_BYTES);
            System.arraycopy(content, SECRET_KEY_BYTES, encryptedData, 0, encryptedData.length);

            // generate secrete key with password and salt
            SecretKeySpec key = genKey(password, salt);
            Cipher cipher = Cipher.getInstance(AES_GCM_PKCS_5_PADDING);
            // initial IV parameter
            AlgorithmParameters params = AlgorithmParameters.getInstance(AES);
            params.init(new IvParameterSpec(new byte[SECRET_KEY_BYTES]));
            // decryption
            cipher.init(Cipher.DECRYPT_MODE, key, params);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new EncryptionException("decryption error", e);
        }
    }
}
