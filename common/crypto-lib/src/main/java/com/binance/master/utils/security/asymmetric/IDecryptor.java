package com.binance.master.utils.security.asymmetric;

import com.binance.master.utils.security.common.EncryptionException;

public interface IDecryptor {

    /**
     * decrypt the data.
     *
     * @param encryptedData
     * @return
     * @throws EncryptionException
     */
    byte[] decrypt(byte[] encryptedData) throws EncryptionException;
}
