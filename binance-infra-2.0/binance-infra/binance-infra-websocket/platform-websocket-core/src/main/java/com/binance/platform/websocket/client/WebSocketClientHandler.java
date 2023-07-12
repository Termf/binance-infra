package com.binance.platform.websocket.client;

import com.binance.platform.websocket.support.WebSocketSession;

import io.netty.handler.codec.http.HttpResponse;

/**
 * WebSocket 客户端监听接口
 * 
 * @author liushiming
 *
 */
public interface WebSocketClientHandler {

	default void handshake(WebSocketSession session, HttpResponse response) {
	}

	default void onClose(WebSocketSession session) {
	}

	default void onMessage(WebSocketSession session, String message) {
	}

	default void onBinary(WebSocketSession session, byte[] bytes) {
	}
}
