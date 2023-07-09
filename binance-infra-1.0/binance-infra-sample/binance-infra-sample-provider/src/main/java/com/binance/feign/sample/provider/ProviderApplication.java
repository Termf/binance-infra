package com.binance.feign.sample.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.binance.feign.sample.provider.config.MyDbConfig;
import com.binance.platform.openfeign.signature.VerifierProvider;
import com.binance.platform.redis.RedisLock;

@SpringBootApplication
@EnableDiscoveryClient
@Import(MyDbConfig.class)
public class ProviderApplication implements CommandLineRunner {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.bootstrap.enabled", "false");
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Autowired
    private RedisLock redisLock;

    @Bean
    public VerifierProvider verifier() {
        return new VerifierProvider() {

            @Override
            public Resource publicKey() {

                return new FileSystemResource("/Users/liushiming/Desktop/test-pub-key.pem");
            }

        };
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            boolean res = redisLock.tryLock("RedisLockKey", 1000, 3000);
            if (res) { // 成功
                // 处理业务
            }
        } finally {
            // 释放锁
            redisLock.unlock();
        }

    }

}
