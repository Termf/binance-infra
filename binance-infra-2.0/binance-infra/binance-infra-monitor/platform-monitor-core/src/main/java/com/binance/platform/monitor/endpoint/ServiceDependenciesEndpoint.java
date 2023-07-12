package com.binance.platform.monitor.endpoint;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.binance.platform.monitor.model.MicroServiceDepency;

/**
 * 服务依赖拓扑图
 * 
 * @author liushiming
 *
 */
@RestControllerEndpoint(id = "servicedeps")
public class ServiceDependenciesEndpoint {

    public ServiceDependenciesEndpoint() {}

    public static final MicroServiceDepency SERVICEDEPENCY = new MicroServiceDepency();

    @RequestMapping(method = {RequestMethod.GET})
    public MicroServiceDepency microservicedeps() {
        return SERVICEDEPENCY;
    }
}
