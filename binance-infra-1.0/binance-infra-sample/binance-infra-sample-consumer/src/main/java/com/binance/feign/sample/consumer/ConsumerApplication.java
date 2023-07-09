package com.binance.feign.sample.consumer;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.binance.platform.openfeign.signature.SignerProvider;
import com.google.common.collect.Maps;

@EnableFeignClients(basePackages = "com.binance.feign.sample.provider.service")
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApplication {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.bootstrap.enabled", "false");
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public SignerProvider signer() {
        return new SignerProvider() {

            @Override
            public Map<String, Resource> privateKey() {
                Map<String, Resource> privateKeys = Maps.newHashMap();
                privateKeys.put("sample-provider",
                    new FileSystemResource("/Users/liushiming/Desktop/test-prv-key.pem"));
                return privateKeys;

            }

        };
    }
}
