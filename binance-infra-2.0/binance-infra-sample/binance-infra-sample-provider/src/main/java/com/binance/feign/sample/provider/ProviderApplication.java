
package com.binance.feign.sample.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.binance.platform.redis.RedisLock;

@SpringBootApplication
@EnableDiscoveryClient
public class ProviderApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    public void setKey(String key, String value) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    @Autowired
    private RedisLock redisLock;

    @Override
    public void run(String... args) throws Exception {

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("test", "test");

        operations.get("test");
        
        
        try {
            boolean res = redisLock.tryLock("RedisLockKey111222334123123", 1000, 3000);
            if (res) {
                System.out.print("获取锁成功");
            }
        } finally {
            // 释放锁
            redisLock.unlock("RedisLockKey111222334123123");
        }

    }

}
