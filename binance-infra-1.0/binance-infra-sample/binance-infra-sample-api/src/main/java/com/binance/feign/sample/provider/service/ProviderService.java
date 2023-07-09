package com.binance.feign.sample.provider.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.binance.feign.sample.provider.vo.Pojo;
import com.binance.platform.openfeign.signature.IntraSecurity;

@FeignClient("sample-provider")
@IntraSecurity
public interface ProviderService {
    @PostMapping("/v1/hello")
    String hello(@RequestBody Pojo pojo);
}
