package com.binance.platform.openfeign.signature;

import org.springframework.core.io.Resource;

public interface VerifierProvider {

    /**
     * 暴露远程服务的公钥匙
     * 
     * @return 当前启动的服务的公钥
     */
    Resource publicKey();

}
