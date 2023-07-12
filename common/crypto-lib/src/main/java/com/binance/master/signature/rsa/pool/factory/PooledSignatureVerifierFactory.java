package com.binance.master.signature.rsa.pool.factory;

import com.binance.master.signature.rsa.RSASignatureVerifier;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PooledSignatureVerifierFactory implements PooledObjectFactory<RSASignatureVerifier> {

    private final String publicKeyStr;

    public PooledSignatureVerifierFactory(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
    }

    @Override
    public PooledObject<RSASignatureVerifier> makeObject() throws Exception {
        return new DefaultPooledObject<>(new RSASignatureVerifier(publicKeyStr));
    }

    @Override
    public void destroyObject(PooledObject<RSASignatureVerifier> pooledObject) throws Exception {
        pooledObject.deallocate();
    }

    @Override
    public boolean validateObject(PooledObject<RSASignatureVerifier> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<RSASignatureVerifier> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<RSASignatureVerifier> pooledObject) throws Exception {

    }
}
