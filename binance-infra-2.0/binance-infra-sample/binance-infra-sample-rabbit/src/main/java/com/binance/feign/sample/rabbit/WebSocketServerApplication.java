package com.binance.feign.sample.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.binance.platform.websocket.server.EnableWebSocketServer;
import com.binance.platform.websocket.server.WebSocketPushService;

@SpringBootApplication
@EnableWebSocketServer
@EnableScheduling
public class WebSocketServerApplication {

	@Autowired
	private WebSocketPushService webSocketPushClient;

	public static void main(String[] args) {
		SpringApplication.run(WebSocketServerApplication.class, args);
	}

	@Scheduled(fixedDelay = 1000L)
	public void send() {
		webSocketPushClient.broadcastTextMessage("helloword cluster");
	}
}
