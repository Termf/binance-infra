package com.binance.platform.websocket.server;

import java.util.List;

import com.binance.platform.websocket.server.cluster.MessagePubSubService;
import com.binance.platform.websocket.server.cluster.WebSocketChannelRepository;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketPushService {

	private final WebSocketChannelRepository webSocketChannelRepository;

	private final MessagePubSubService redisPubSub;

	public WebSocketPushService() {
		this.webSocketChannelRepository = WebSocketChannelRepository.getInstance();
		this.redisPubSub = new MessagePubSubService();
	}

	/**
	 * 推送文本消息
	 */
	public void sendTextMessage(String message, String sessionId) {
		// 在本节点查询是否有Channel
		Channel channel = webSocketChannelRepository.findLocalChannel(sessionId);
		// 如果查到本机有连接
		if (channel != null) {
			channel.writeAndFlush(new TextWebSocketFrame(message));
		}
		// 从redis里查该sessionId所处于的节点
		else {
			String remoteAddress = webSocketChannelRepository.findRemoteChannelAddress(sessionId);
			redisPubSub.pubTextMessageToRemoteAddress(remoteAddress, sessionId, message);
		}

	}

	/**
	 * 推送字节消息
	 */
	public void sendBinaryMessage(byte[] message, String sessionId) {
		// 在本节点查询是否有Channel
		Channel channel = webSocketChannelRepository.findLocalChannel(sessionId);
		// 如果查到本机有连接
		if (channel != null) {
			ByteBuf buffer = channel.alloc().buffer(message.length);
			channel.writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(message)));
		}
		// 从redis里查该sessionId所处于的节点
		else {
			String remoteAddress = webSocketChannelRepository.findRemoteChannelAddress(sessionId);
			redisPubSub.pubByteMessageToRemoteAddress(remoteAddress, sessionId, message);
		}
	}

	/**
	 * 广播文本消息
	 */
	public void broadcastTextMessage(String textMessage) {
		// 在本节点查询是否有Channel
		List<Channel> channelList = webSocketChannelRepository.findAllLocalChannel();
		for (Channel channel : channelList) {
			if (channel != null) {
				if (textMessage != null) {
					channel.writeAndFlush(new TextWebSocketFrame(textMessage));
				}
			}
		}
		List<String> remoteAddressList = webSocketChannelRepository.findAllRemoteAddress();
		for (String remoteAddress : remoteAddressList) {
			redisPubSub.pubBroadcastTextMessageToRemoteAddress(remoteAddress, textMessage);
		}

	}

	/**
	 * 广播字节消息
	 */
	public void broadcastBinaryMessage(byte[] binaryMessage) {
		// 在本节点查询是否有Channel
		List<Channel> channelList = webSocketChannelRepository.findAllLocalChannel();
		for (Channel channel : channelList) {
			if (channel != null) {
				if (binaryMessage != null) {
					ByteBuf buffer = channel.alloc().buffer(binaryMessage.length);
					channel.writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(binaryMessage)));
				}
			}
		}
		List<String> remoteAddressList = webSocketChannelRepository.findAllRemoteAddress();
		for (String remoteAddress : remoteAddressList) {
			redisPubSub.pubBroadcastByteMessageToRemoteAddress(remoteAddress, binaryMessage);
		}

	}
}
