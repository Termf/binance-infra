package com.binance.platform.websocket.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.websocket.SpringContextHolder;
import com.binance.platform.websocket.support.WebSocketSession;
import com.binance.platform.websocket.util.IdUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;

public class WebSocketClientHandlerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandlerSupport.class);

    public static final AttributeKey<WebSocketSession> SESSION_KEY = AttributeKey.valueOf("WEBSOCKET_SESSION");

    private List<WebSocketClientHandler> webSocketHandlerList =
        SpringContextHolder.getBeans(WebSocketClientHandler.class);

    public void doBeforeHandshake(Channel channel, FullHttpResponse rep) {
        String sessionId = IdUtils.next();
        channel.attr(WebSocketSession.SESSION_ID).set(sessionId);
        WebSocketSession session = new WebSocketSession(channel);
        channel.attr(SESSION_KEY).set(session);
        for (WebSocketClientHandler webSocketHandler : webSocketHandlerList) {
            try {
                webSocketHandler.handshake(session, rep);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void doOnMessage(Channel channel, WebSocketFrame frame) {
        WebSocketSession session = channel.attr(SESSION_KEY).get();
        TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
        String message = textFrame.text();
//        LOGGER.info("Receive message from webSocketserver,the message is: {}, the remoteAddress is: {}", message,
//            channel.remoteAddress() != null ? channel.remoteAddress().toString() : "");
        for (WebSocketClientHandler webSocketHandler : webSocketHandlerList) {
            try {
                webSocketHandler.onMessage(session, message);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void doOnBinary(Channel channel, WebSocketFrame frame) {
        WebSocketSession session = channel.attr(SESSION_KEY).get();
        BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame)frame;
        ByteBuf content = binaryWebSocketFrame.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
//        LOGGER.info("Receive message from webSocketserver,the message is: {}, the remoteAddress is: {}",
//            new String(bytes), channel.remoteAddress() != null ? channel.remoteAddress().toString() : "");
        for (WebSocketClientHandler webSocketHandler : webSocketHandlerList) {
            try {
                webSocketHandler.onBinary(session, bytes);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void doOnClose(Channel channel) {
        WebSocketSession session = channel.attr(SESSION_KEY).get();
        for (WebSocketClientHandler webSocketHandler : webSocketHandlerList) {
            try {
                webSocketHandler.onClose(session);
            } catch (Throwable e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
