package com.binance.platform.websocket.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import io.micrometer.core.instrument.Counter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

@Sharable
class WebSocketClientInboundHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientInboundHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private final WebSocketClientHandlerSupport webSocketHandlerSupport;
    private final Counter messageCount;
    private ChannelPromise handshakeFuture;

    public WebSocketClientInboundHandler(WebSocketClientHandshaker handshaker, Counter messageCount) {
        this.handshaker = handshaker;
        this.webSocketHandlerSupport = new WebSocketClientHandlerSupport();
        this.messageCount = messageCount;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.debug("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MDC.put("traceId", generateTraceId());
            Channel ch = ctx.channel();
            if (!handshaker.isHandshakeComplete()) {
                this.handshaker.finishHandshake(ch, (FullHttpResponse)msg);
                LOGGER.info("WebSocket Client connected!");
                this.handshakeFuture.setSuccess();
                this.webSocketHandlerSupport.doBeforeHandshake(ctx.channel(), (FullHttpResponse)msg);
                return;
            }
            if (msg instanceof FullHttpResponse) {
                return;
            }
            if (msg instanceof WebSocketFrame) {
                messageCount.increment();
                Channel channel = ctx.channel();
                WebSocketFrame frame = (WebSocketFrame)msg;
                frame.content().readableBytes();
                if (frame instanceof TextWebSocketFrame) {
                    TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
                    this.webSocketHandlerSupport.doOnMessage(channel, textFrame);
                } else if (frame instanceof BinaryWebSocketFrame) {
                    BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)frame;
                    this.webSocketHandlerSupport.doOnBinary(channel, binaryFrame);
                } else if (frame instanceof PongWebSocketFrame) {
                    ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
                } else if (frame instanceof CloseWebSocketFrame) {
                    this.webSocketHandlerSupport.doOnClose(channel);
                }
            }
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (!this.handshakeFuture.isDone()) {
            this.handshakeFuture.setFailure(cause);
        }
    }

    public static String generateTraceId() {
        String s = UUID.randomUUID().toString();
        StringBuilder builder = new StringBuilder("WebSocket:");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            builder.append((c == '-') ? "" : c);
        }
        return builder.toString();
    }

}
