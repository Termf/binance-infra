package com.binance.master.signature.rsa;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

import com.binance.master.signature.ISigner;

/**
 * @author james.li
 */
public class RSASigner implements ISigner {

    private RSADigestSigner signer;

    private void loadSigner(String privateKeyStr) throws IOException {
        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new StringReader(privateKeyStr));
            PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
            AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(pemKeyPair.getPrivateKeyInfo());
            signer = new RSADigestSigner(new SHA256Digest());
            signer.init(true, privateKey);
        } finally {
            if (pemParser != null) {
                pemParser.close();
            }
        }
    }

    public RSASigner(String privateKeyStr) throws IOException {
        loadSigner(privateKeyStr);
    }


    @Override
    public String doSign(byte[] body) throws CryptoException {
        if (signer == null) {
            throw new CryptoException("cannot load the signer");
        }
        byte[] signature;
        synchronized (this) {
            signer.update(body, 0, body.length);
            signature = signer.generateSignature();
        }
        return Base64.getEncoder().encodeToString(signature);
    }
}
