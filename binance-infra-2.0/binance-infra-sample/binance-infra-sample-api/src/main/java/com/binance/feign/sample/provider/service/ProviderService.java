package com.binance.feign.sample.provider.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.binance.feign.sample.provider.vo.Pojo;
import com.binance.platform.eureka.RedefineFeignClient;

@FeignClient(name = "sample-provider")
public interface ProviderService {

    @GetMapping("/bau/is-in-privacy-coin-check-list")
    @RedefineFeignClient(name = "test")
    Pojo hello();
}
