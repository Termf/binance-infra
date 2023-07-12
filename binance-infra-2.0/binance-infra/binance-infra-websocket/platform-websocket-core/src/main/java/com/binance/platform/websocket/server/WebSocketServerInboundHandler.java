package com.binance.platform.websocket.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketServerInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerInboundHandler.class);

	private final WebSocketServerHandlerSupport webSocketHandlerSupport;

	public WebSocketServerInboundHandler(WebSocketServerHandlerSupport webSocketHandlerSupport) {
		this.webSocketHandlerSupport = webSocketHandlerSupport;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		handleWebSocketFrame(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		webSocketHandlerSupport.doOnError(ctx.channel(), cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		webSocketHandlerSupport.doOnClose(ctx.channel());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		webSocketHandlerSupport.doOnEvent(ctx.channel(), evt);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		if (ctx.channel().isWritable()) {
			LOGGER.debug("Channel is writable, resume read.");
			ctx.channel().config().setAutoRead(true);
		} else {
			LOGGER.debug("Channel is not writable, pause read.");
			ctx.channel().config().setAutoRead(false);
		}
		super.channelWritabilityChanged(ctx);
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof TextWebSocketFrame) {
			webSocketHandlerSupport.doOnMessage(ctx.channel(), frame);
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (frame instanceof CloseWebSocketFrame) {
			ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		if (frame instanceof BinaryWebSocketFrame) {
			webSocketHandlerSupport.doOnBinary(ctx.channel(), frame);
			return;
		}
		if (frame instanceof PongWebSocketFrame) {
			return;
		}
	}

}
