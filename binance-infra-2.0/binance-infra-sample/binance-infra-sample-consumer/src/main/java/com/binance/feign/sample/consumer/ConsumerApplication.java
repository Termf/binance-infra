package com.binance.feign.sample.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.binance.platform.websocket.server.EnableWebSocketServer;

@SpringBootApplication
@EnableWebSocketServer
public class ConsumerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("spring.cloud.bootstrap.enabled", "false");
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Server has started!");
    }

}
