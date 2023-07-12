package com.binance.platform.openfeign.signature;

import java.util.Map;

import org.springframework.core.io.Resource;

public interface SignerProvider {

    /**
     * 每个服务隔离的PrivateKey
     * 
     * @return key：服务ID，value：私钥
     */
    Map<String, Resource> isolatePrivateKey();

    /**
     * 所有服务共用PrivateKey
     * 
     * @return
     */
    Resource shareprivateKey();

}
