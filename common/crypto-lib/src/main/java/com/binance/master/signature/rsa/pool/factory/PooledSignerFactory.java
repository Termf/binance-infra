package com.binance.master.signature.rsa.pool.factory;

import com.binance.master.signature.rsa.RSASigner;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PooledSignerFactory implements PooledObjectFactory<RSASigner> {

    private final String privateKeyStr;

    public PooledSignerFactory(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
    }

    @Override
    public PooledObject<RSASigner> makeObject() throws Exception {
        return new DefaultPooledObject<>(new RSASigner(privateKeyStr));
    }

    @Override
    public void destroyObject(PooledObject<RSASigner> pooledObject) throws Exception {
        pooledObject.deallocate();
    }

    @Override
    public boolean validateObject(PooledObject<RSASigner> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<RSASigner> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<RSASigner> pooledObject) throws Exception {

    }
}
