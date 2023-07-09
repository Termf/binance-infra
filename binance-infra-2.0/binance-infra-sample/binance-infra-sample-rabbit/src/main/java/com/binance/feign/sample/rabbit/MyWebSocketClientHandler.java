package com.binance.feign.sample.rabbit;

import org.springframework.stereotype.Component;

import com.binance.platform.websocket.client.WebSocketClientHandler;
import com.binance.platform.websocket.support.WebSocketSession;

@Component
public class MyWebSocketClientHandler implements WebSocketClientHandler {

	@Override
	public void onMessage(WebSocketSession session, String message) {
		System.out.println(message);
		// session.sendText(message + "HelloWord");
	}
}
