package com.binance.platform.websocket.server;

import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.springframework.beans.TypeMismatchException;

import com.binance.platform.websocket.WebSocketProperties;
import com.binance.platform.websocket.server.cluster.WebSocketChannelRepository;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

@SuppressWarnings("deprecation")
class HttpServerInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final WebSocketProperties webSocketProperties;

	private final WebSocketServerHandlerSupport webSocketHandlerSupport;

	private static final String NEWLINE = "\r\n";

	private final WebSocketChannelRepository webSocketChannelRepository;

	public HttpServerInboundHandler(WebSocketProperties webSocketProperties) {
		this.webSocketProperties = webSocketProperties;
		this.webSocketHandlerSupport = new WebSocketServerHandlerSupport();
		this.webSocketChannelRepository = WebSocketChannelRepository.getInstance();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
		try {
			handleHttpRequest(ctx, msg);
		} catch (TypeMismatchException e) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
			sendHttpResponse(ctx, msg, res);
		} catch (Throwable e) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
			sendHttpResponse(ctx, msg, res);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		webSocketHandlerSupport.doOnError(ctx.channel(), cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		webSocketHandlerSupport.doOnClose(ctx.channel());
		super.channelInactive(ctx);
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// Allow only GET methods.
		if (req.method() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}
		// Send the demo page and favicon.ico
		if ("/".equals(req.uri())) {
			ByteBuf contentByteBuf = getIndexContent(getWebSocketLocation(req));
			DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, contentByteBuf);
			res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
			if (HttpHeaders.isKeepAlive(req)) {
				res.headers().set(CONNECTION, Values.KEEP_ALIVE);
			}
			setContentLength(res, contentByteBuf.readableBytes());
			sendHttpResponse(ctx, req, res);
			return;
		}
		if ("/favicon.ico".equals(req.uri())) {
			DefaultFullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
			sendHttpResponse(ctx, req, res);
			return;
		}
		Channel channel = ctx.channel();
		webSocketHandlerSupport.doBeforeHandshake(channel, req);
		if (!channel.isActive()) {
			return;
		}
		// Handshake
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
				null, true, 65536);
		WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
		} else {
			ChannelPipeline pipeline = ctx.pipeline();
			pipeline.remove(ctx.name());
			if (webSocketProperties.getServer().getReaderIdleTimeSeconds() != 0
					|| webSocketProperties.getServer().getWriterIdleTimeSeconds() != 0
					|| webSocketProperties.getServer().getAllIdleTimeSeconds() != 0) {
				pipeline.addLast(new IdleStateHandler(webSocketProperties.getServer().getReaderIdleTimeSeconds(),
						webSocketProperties.getServer().getWriterIdleTimeSeconds(),
						webSocketProperties.getServer().getAllIdleTimeSeconds()));
			}
			pipeline.addLast(new WebSocketFrameAggregator(Integer.MAX_VALUE));
			pipeline.addLast(new WebSocketServerInboundHandler(webSocketHandlerSupport));
			handshaker.handshake(channel, req).addListener(future -> {
				if (future.isSuccess()) {
					webSocketHandlerSupport.doOnOpen(channel, req);
					webSocketChannelRepository.saveChannelToLocalAndRemote(channel);
				} else {
					handshaker.close(channel, new CloseWebSocketFrame());
				}
			});
		}

	}

	private ByteBuf getIndexContent(String webSocketLocation) {
		return Unpooled.copiedBuffer("<html><head><title>Web Socket Test</title></head>" + NEWLINE + "<body>" + NEWLINE
				+ "<script type=\"text/javascript\">" + NEWLINE + "var socket;" + NEWLINE + "if (!window.WebSocket) {"
				+ NEWLINE + "  window.WebSocket = window.MozWebSocket;" + NEWLINE + '}' + NEWLINE
				+ "if (window.WebSocket) {" + NEWLINE + "  socket = new WebSocket(\"" + webSocketLocation + "\");"
				+ NEWLINE + "  socket.onmessage = function(event) {" + NEWLINE
				+ "    var ta = document.getElementById('responseText');" + NEWLINE
				+ "    ta.value = ta.value + '\\n' + event.data" + NEWLINE + "  };" + NEWLINE
				+ "  socket.onopen = function(event) {" + NEWLINE
				+ "    var ta = document.getElementById('responseText');" + NEWLINE
				+ "    ta.value = \"Web Socket opened!\";" + NEWLINE + "  };" + NEWLINE
				+ "  socket.onclose = function(event) {" + NEWLINE
				+ "    var ta = document.getElementById('responseText');" + NEWLINE + "    ta.value = ta.value + "
				+ NEWLINE + "\"Web Socket closed\"; " + NEWLINE + "  };" + NEWLINE + "} else {" + NEWLINE
				+ "  alert(\"Your browser does not support Web Socket.\");" + NEWLINE + '}' + NEWLINE + NEWLINE
				+ "function send(message) {" + NEWLINE + "  if (!window.WebSocket) { return; }" + NEWLINE
				+ "  if (socket.readyState == WebSocket.OPEN) {" + NEWLINE + "    socket.send(message);" + NEWLINE
				+ "  } else {" + NEWLINE + "    alert(\"The socket is not open.\");" + NEWLINE + "  }" + NEWLINE + '}'
				+ NEWLINE + "</script>" + NEWLINE + "<form onsubmit=\"return false;\">" + NEWLINE
				+ "<input type=\"text\" name=\"message\" value=\"Hello, World!\"/>"
				+ "<input type=\"button\" value=\"Send Web Socket Data\"" + NEWLINE
				+ "       onclick=\"send(this.form.message.value)\" />" + NEWLINE + "<h3>Output</h3>" + NEWLINE
				+ "<textarea id=\"responseText\" style=\"width:500px;height:300px;\"></textarea>" + NEWLINE + "</form>"
				+ NEWLINE + "</body>" + NEWLINE + "</html>" + NEWLINE, CharsetUtil.UTF_8);

	}

	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		int statusCode = res.status().code();
		if (statusCode != OK.code() && res.content().readableBytes() == 0) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		HttpUtil.setContentLength(res, res.content().readableBytes());
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpUtil.isKeepAlive(req) || statusCode != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private String getWebSocketLocation(FullHttpRequest req) {
		String location = req.headers().get(HttpHeaderNames.HOST) + webSocketProperties.getServer().getEndpoint();
		return "ws://" + location;
	}

}
