package com.binance.feign.sample.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binance.feign.sample.provider.service.ProviderService;
import com.binance.feign.sample.provider.vo.Pojo;

@RestController
public class TestService {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/test")
    public String test() {
        Pojo pojo = new Pojo();
        pojo.setName("charles");
        pojo.setAge(18);
        return "Test " + providerService.hello(pojo);
    }

}