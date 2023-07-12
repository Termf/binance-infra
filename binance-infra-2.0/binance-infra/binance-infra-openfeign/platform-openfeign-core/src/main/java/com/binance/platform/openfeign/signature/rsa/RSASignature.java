package com.binance.platform.openfeign.signature.rsa;

import java.io.InputStreamReader;
import java.util.Base64;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.crmf.CRMFRuntimeException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;

public interface RSASignature {

    public static final String SIGNATURE_SWITCH = "com.binance.intra.security.switch";

    public static final String INTRA_SECURITY_SIGNATURE_HEADER_KEY = "intra-sig";

    default String calculateKey(String method, String path) {
        return method + "#" + path;
    }

    static class RSASignerInner {

        private final AsymmetricKeyParameter privateKey;

        public RSASignerInner(final InputStreamReader privateKey) {
            try (PEMParser pemParser = new PEMParser(privateKey)) {
                PEMKeyPair pemKeyPair = (PEMKeyPair)pemParser.readObject();
                this.privateKey = PrivateKeyFactory.createKey(pemKeyPair.getPrivateKeyInfo());
            } catch (Throwable e) {
                throw new RuntimeCryptoException(e.getMessage());
            }
        }

        public String doSign(byte[] body) {
            byte[] signature;
            try {
                RSADigestSigner createSig = new RSADigestSigner(new SHA256Digest());
                createSig.init(true, privateKey);
                createSig.update(body, 0, body.length);
                signature = createSig.generateSignature();
            } catch (CryptoException e) {
                throw new RuntimeCryptoException(e.getMessage());
            }
            return Base64.getEncoder().encodeToString(signature);
        }
    }

    static class RSAVerifierInner {

        private final AsymmetricKeyParameter publicKey;

        public RSAVerifierInner(final InputStreamReader publicKey) {
            try (PEMParser pemParser = new PEMParser(publicKey)) {
                SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo)pemParser.readObject();
                this.publicKey = PublicKeyFactory.createKey(publicKeyInfo);
            } catch (Throwable e) {
                throw new CRMFRuntimeException(e.getMessage(), e);
            }
        }

        public boolean verify(byte[] body, String signature) {
            RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
            signer.init(false, publicKey);
            signer.update(body, 0, body.length);
            return signer.verifySignature(Base64.getDecoder().decode(signature));
        }

    }

}
