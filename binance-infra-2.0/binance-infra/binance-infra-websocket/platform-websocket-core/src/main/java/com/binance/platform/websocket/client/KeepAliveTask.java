package com.binance.platform.websocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

class KeepAliveTask implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(KeepAliveTask.class);
	private Channel ch;
	private boolean stayAlive = true;

	public KeepAliveTask(Channel ch) {
		this.ch = ch;
	}

	@Override
	public void run() {
		while (stayAlive) {
			WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
			if (ch != null && ch.isOpen())
				ch.writeAndFlush(frame);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.info("Shutting down ping pong thread due to interruption.");
			}
		}
	}

	public void stop() {
		stayAlive = false;
	}
}
