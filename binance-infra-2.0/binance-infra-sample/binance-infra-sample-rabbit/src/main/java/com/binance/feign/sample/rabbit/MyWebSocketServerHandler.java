package com.binance.feign.sample.rabbit;

import org.springframework.stereotype.Component;

import com.binance.platform.websocket.server.WebSocketServerHandler;
import com.binance.platform.websocket.support.WebSocketSession;

@Component
public class MyWebSocketServerHandler implements WebSocketServerHandler {
	@Override
	public void onMessage(WebSocketSession session, String message) {
		session.sendText(message + "HelloWord");
	}
}
