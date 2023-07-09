package com.binance.feign.sample.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.binance.platform.websocket.client.WebSocketClient;

@SpringBootApplication
@EnableScheduling
public class WebSocketClientApplication {

	@Autowired
	private WebSocketClient webSocketClient;

//	@Scheduled(fixedDelay = 1000L)
//	public void send() {
//		webSocketClient.sendTextMessage("shiming ");
//	}

	public static void main(String[] args) {
		SpringApplication.run(WebSocketClientApplication.class, args);
	}
}
