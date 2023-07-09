package com.binance.infra.sample.cache.controller;

import com.binance.infra.sample.cache.model.Response;
import com.binance.platform.redis.RedisLock;
import com.binance.platform.redis.lock.jedis.ConsistentHashJedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-19
 */
@RestController
@RequestMapping("lock")
public class LockController {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private ConsistentHashJedisLock consistentHashJedisLock;

    @GetMapping("default")
    public Response<String> acquire(){
        try {
            System.out.println("trying to lock...");
            redisLock.lock();
        } finally {
            redisLock.unlock();
            System.out.println("lock released.");
        }
        return Response.success("success");
    }

    @GetMapping("redlock")
    public Response<String> tryLock(){
        //100是等待锁获取时间，3000是锁的expireTime，单位是毫秒
        boolean locked = redisLock.tryLock("demo-lock", 100, 3000);
        try {
            if (locked) {
                Thread.sleep(200L);
                // 成功, 处理业务
            } else {
                // 获取锁失败的逻辑
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLock.unlock("demo-lock");
        }
        return Response.success("success");
    }

    @GetMapping("consistent")
    public Response<String> consistentHashLock(){
        //100是等待锁获取时间，3000是锁的expireTime，单位是毫秒
        boolean locked = consistentHashJedisLock.tryLock("demo-consistent-lock", 100, 3000);
        try {
            if (locked) {
                Thread.sleep(200L);
                // 成功, 处理业务
            } else {
                // 获取锁失败的逻辑
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(locked) {
                redisLock.unlock("demo-consistent-lock");
            }
        }
        return Response.success("success");
    }

}
