package com.binance.feign.sample.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.binance.platform.websocket.server.WebSocketPushService;
import com.binance.platform.websocket.server.WebSocketServerHandler;
import com.binance.platform.websocket.support.WebSocketSession;

@Component
public class MyWebSocketServerHandler implements WebSocketServerHandler {

    @Autowired
    private WebSocketPushService webSocketPushService;

    @Override
    public void onMessage(WebSocketSession session, String message) {
        webSocketPushService.sendTextMessage(message, session.getSessionId());
    }

    public void sendAll() {
        webSocketPushService.broadcastTextMessage("test");
    }

}
