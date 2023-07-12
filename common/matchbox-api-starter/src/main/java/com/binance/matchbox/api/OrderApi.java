package com.binance.matchbox.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.binance.master.constant.Constant;
import com.binance.matchbox.support.BeanConfig;

@FeignClient(name = Constant.MATCHBOX_WEB_SERVICE, url = "${com.binance.matchbox.api.url}",
    configuration = BeanConfig.class)
public interface OrderApi {

}
