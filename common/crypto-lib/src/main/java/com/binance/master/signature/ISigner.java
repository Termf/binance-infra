package com.binance.master.signature;

import org.bouncycastle.crypto.CryptoException;

/**
 * @author james.li
 */
public interface ISigner {

    /**
     * generate a Base64 formatted signature for the specified body.
     *
     * @param body
     * @return
     * @throws CryptoException
     */
    String doSign(byte[] body) throws CryptoException;
}
