package com.binance.platform.websocket.server;

import com.binance.platform.websocket.support.WebSocketSession;

import io.netty.handler.codec.http.HttpRequest;

/**
 * WebSocket 服务端监听接口
 * 
 * @author liushiming
 *
 */
public interface WebSocketServerHandler {

	default void handshake(WebSocketSession session, HttpRequest request) {
	}

	default void onOpen(WebSocketSession session, HttpRequest request) {
	}

	default void onClose(WebSocketSession session) {
	}

	default void onError(WebSocketSession session, Throwable throwable) {
	}

	default void onBinary(WebSocketSession session, byte[] bytes) {
	}

	default void onMessage(WebSocketSession session, String message) {
	}

	default void onEvent(WebSocketSession session, Object evt) {
	}

}
