package com.binance.platform.monitor.health;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.boot.actuate.health.HealthStatusHttpMapper;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import com.binance.platform.event.ConfigChangeEvent;

public class SwithHealthCheck implements ApplicationListener<ConfigChangeEvent> {

    private static final String SWITHHEALTHCHECK_KEY = "management.health.status.enable";

    @Autowired
    private HealthStatusHttpMapper healthStatusHttpMapper;

    @Autowired
    private Environment environment;

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        String swither = environment.getProperty(SWITHHEALTHCHECK_KEY, "false");
        if (BooleanUtils.toBoolean(swither)) {
            healthStatusHttpMapper.addStatusMapping(Status.DOWN, WebEndpointResponse.STATUS_OK);
            healthStatusHttpMapper.addStatusMapping(Status.OUT_OF_SERVICE, WebEndpointResponse.STATUS_OK);
            healthStatusHttpMapper.addStatusMapping(Status.UNKNOWN, WebEndpointResponse.STATUS_OK);
        } else {
            healthStatusHttpMapper.addStatusMapping(Status.DOWN, WebEndpointResponse.STATUS_SERVICE_UNAVAILABLE);
            healthStatusHttpMapper.addStatusMapping(Status.OUT_OF_SERVICE,
                WebEndpointResponse.STATUS_SERVICE_UNAVAILABLE);
            healthStatusHttpMapper.addStatusMapping(Status.UNKNOWN, WebEndpointResponse.STATUS_OK);
        }
    }

}
