package com.binance.platform.sapi.core;

import com.binance.platform.sapi.exception.ServiceInstanceChooseException;

import java.net.URI;

/**
 * @author 陈添明
 */
public class NoValidServiceInstanceChooser implements ServiceInstanceChooser {


    /**
     * Chooses a ServiceInstance URI from the LoadBalancer for the specified service.
     *
     * @param serviceId The service ID to look up the LoadBalancer.
     * @return Return the uri of ServiceInstance
     */
    @Override
    public URI choose(String serviceId) {
        throw new ServiceInstanceChooseException("No valid service instance selector, Please configure it!");
    }
}
