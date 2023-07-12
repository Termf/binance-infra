package com.binance.master.utils.security.asymmetric;

import com.binance.master.utils.security.asymmetric.encryption.DecryptorImpl;
import com.binance.master.utils.security.asymmetric.encryption.EncryptorImpl;

public class CryptoFactory {

    /**
     *
     * @param publicKey
     * @return
     */
    public static IEncryptor createEncryptor(String publicKey) {
        return new EncryptorImpl(publicKey);
    }

    /**
     *
     * @param privateKey
     * @return
     */
    public static IDecryptor createDecryptor(String privateKey) {
        return new DecryptorImpl(privateKey);
    }
}
