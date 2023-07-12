package com.binance.platform.websocket.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.websocket.SpringContextHolder;
import com.binance.platform.websocket.support.WebSocketSession;
import com.binance.platform.websocket.util.IdUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;

public class WebSocketServerHandlerSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerHandlerSupport.class);

	public static final AttributeKey<WebSocketSession> SESSION_KEY = AttributeKey.valueOf("WEBSOCKET_SESSION");

	private List<WebSocketServerHandler> webSocketHandlerList = SpringContextHolder
			.getBeans(WebSocketServerHandler.class);

	public void doBeforeHandshake(Channel channel, FullHttpRequest req) {
		String sessionId = IdUtils.next();
		channel.attr(WebSocketSession.SESSION_ID).set(sessionId);
		WebSocketSession session = new WebSocketSession(channel);
		channel.attr(SESSION_KEY).set(session);
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.handshake(session, req);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnOpen(Channel channel, FullHttpRequest req) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onOpen(session, req);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnClose(Channel channel) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onClose(session);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnError(Channel channel, Throwable throwable) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onError(session, throwable);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnMessage(Channel channel, WebSocketFrame frame) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onMessage(session, textFrame.text());
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnBinary(Channel channel, WebSocketFrame frame) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
		ByteBuf content = binaryWebSocketFrame.content();
		byte[] bytes = new byte[content.readableBytes()];
		content.readBytes(bytes);
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onBinary(session, bytes);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public void doOnEvent(Channel channel, Object event) {
		WebSocketSession session = channel.attr(SESSION_KEY).get();
		for (WebSocketServerHandler webSocketHandler : webSocketHandlerList) {
			try {
				webSocketHandler.onEvent(session, event);
			} catch (Throwable e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
