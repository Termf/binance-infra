package com.binance.master.utils.security.asymmetric;

import com.binance.master.utils.security.common.EncryptionException;

/**
 *
 */
public interface IEncryptor {

    /**
     * encrypt the data.
     *
     * @param data
     * @return
     * @throws EncryptionException
     */
    byte[] encrypt(byte[] data) throws EncryptionException;
}
