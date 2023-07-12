package com.binance.platform.websocket.support;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class WebSocketSession {

	public static final AttributeKey<String> SESSION_ID = AttributeKey.valueOf("WEBSOCKET_SESSION_ID");

	private final Channel channel;
	private final String sessionId;

	public WebSocketSession(Channel channel) {
		this.channel = channel;
		this.sessionId = channel.attr(SESSION_ID).get();
	}

	public ChannelFuture sendText(String message) {
		return channel.writeAndFlush(new TextWebSocketFrame(message));
	}

	public ChannelFuture sendText(ByteBuf byteBuf) {
		return channel.writeAndFlush(new TextWebSocketFrame(byteBuf));
	}

	public ChannelFuture sendText(ByteBuffer byteBuffer) {
		ByteBuf buffer = channel.alloc().buffer(byteBuffer.remaining());
		buffer.writeBytes(byteBuffer);
		return channel.writeAndFlush(new TextWebSocketFrame(buffer));
	}

	public ChannelFuture sendText(TextWebSocketFrame textWebSocketFrame) {
		return channel.writeAndFlush(textWebSocketFrame);
	}

	public ChannelFuture sendBinary(byte[] bytes) {
		ByteBuf buffer = channel.alloc().buffer(bytes.length);
		return channel.writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(bytes)));
	}

	public ChannelFuture sendBinary(ByteBuf byteBuf) {
		return channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
	}

	public ChannelFuture sendBinary(ByteBuffer byteBuffer) {
		ByteBuf buffer = channel.alloc().buffer(byteBuffer.remaining());
		buffer.writeBytes(byteBuffer);
		return channel.writeAndFlush(new BinaryWebSocketFrame(buffer));
	}

	public ChannelFuture sendBinary(BinaryWebSocketFrame binaryWebSocketFrame) {
		return channel.writeAndFlush(binaryWebSocketFrame);
	}

	public <T> void setAttribute(String name, T value) {
		AttributeKey<T> sessionIdKey = AttributeKey.valueOf(name);
		channel.attr(sessionIdKey).set(value);
	}

	public <T> T getAttribute(String name) {
		AttributeKey<T> sessionIdKey = AttributeKey.valueOf(name);
		return channel.attr(sessionIdKey).get();
	}

	public Channel channel() {
		return channel;
	}

	public ChannelId id() {
		return channel.id();
	}

	public ChannelConfig config() {
		return channel.config();
	}

	public boolean isOpen() {
		return channel.isOpen();
	}

	public boolean isRegistered() {
		return channel.isRegistered();
	}

	public boolean isActive() {
		return channel.isActive();
	}

	public ChannelMetadata metadata() {
		return channel.metadata();
	}

	public SocketAddress localAddress() {
		return channel.localAddress();
	}

	public SocketAddress remoteAddress() {
		return channel.remoteAddress();
	}

	public ChannelFuture closeFuture() {
		return channel.closeFuture();
	}

	public boolean isWritable() {
		return channel.isWritable();
	}

	public long bytesBeforeUnwritable() {
		return channel.bytesBeforeUnwritable();
	}

	public long bytesBeforeWritable() {
		return channel.bytesBeforeWritable();
	}

	public Channel.Unsafe unsafe() {
		return channel.unsafe();
	}

	public ChannelPipeline pipeline() {
		return channel.pipeline();
	}

	public ByteBufAllocator alloc() {
		return channel.alloc();
	}

	public Channel read() {
		return channel.read();
	}

	public Channel flush() {
		return channel.flush();
	}

	public ChannelFuture close() {
		return channel.close();
	}

	public ChannelFuture close(ChannelPromise promise) {
		return channel.close(promise);
	}

	public String getSessionId() {
		return sessionId;
	}

}
