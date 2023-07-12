package com.binance.master.signature;

/**
 * @author james.li
 */
public interface ISignatureVerifier {

    /**
     * to verify whether the specified signature is valid or not for the body.
     *
     * @param body
     * @param signature
     * @return
     */
    boolean verifySignature(byte[] body, String signature);
}
