package com.binance.master.signature.rsa;

import com.binance.master.signature.ISignatureVerifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;


/**
 * @author james.li
 */
public class RSASignatureVerifier implements ISignatureVerifier {


    private final AsymmetricKeyParameter pubKey;

    /**
     * 根据公钥创建RSASignatureVerifier，从而可以通过对公钥信息进行验签。
     *
     * @param pubKeyStr 公钥（PKCS#1格式）.
     * @throws IOException
     */
    public RSASignatureVerifier(String pubKeyStr) throws IOException {
        PEMParser pubParser = null;
        try {
            // public key必须为PEM格式，否则无法解析
            pubParser = new PEMParser(new StringReader(pubKeyStr));
            SubjectPublicKeyInfo pubKeyObj = (SubjectPublicKeyInfo) pubParser.readObject();
            pubKey = PublicKeyFactory.createKey(pubKeyObj);
        } finally {
            if (pubParser != null) {
                pubParser.close();
            }
        }
    }

    @Override
    public boolean verifySignature(byte[] body, String signature) {
        if (pubKey == null) {
            return false;
        }
        RSADigestSigner verifier = new RSADigestSigner(new SHA256Digest());
        verifier.init(false, pubKey);
        verifier.update(body, 0, body.length);
        try {
            return verifier.verifySignature(Base64.getDecoder().decode(signature));
        } catch (Throwable e) {
            return false;
        }
    }
}
