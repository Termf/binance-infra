package com.binance.master.signature.rsa.pool;

import com.binance.master.signature.ISigner;
import com.binance.master.signature.rsa.RSASigner;
import com.binance.master.signature.rsa.pool.factory.PooledSignerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bouncycastle.crypto.CryptoException;

@Slf4j
public class PooledRSASigner implements ISigner {

    private final GenericObjectPool<RSASigner> pool;

    public PooledRSASigner(String privateKeyStr) {
        this(privateKeyStr, 20, 10);
    }

    public PooledRSASigner(String privateKeyStr, int maxTotal, int maxIdle) {
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(maxTotal > 0 ? maxTotal : 20);
        conf.setMaxIdle(maxIdle < maxTotal ? maxIdle : 10);
        pool = new GenericObjectPool<>(new PooledSignerFactory(privateKeyStr), conf);
    }

    @Override
    public String doSign(byte[] body) throws CryptoException {
        RSASigner signer = null;
        try {
            signer = pool.borrowObject();
            return signer.doSign(body);
        } catch (CryptoException e) {
            throw e;
        } catch (Exception e) {
            log.error("cannot borrow RSASigner from pool", e);
            throw new CryptoException("unexpected error", e);
        } finally {
            if (signer != null) {
                pool.returnObject(signer);
            }
        }
    }
}
