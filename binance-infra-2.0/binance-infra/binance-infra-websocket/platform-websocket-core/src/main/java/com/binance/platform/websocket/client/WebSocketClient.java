package com.binance.platform.websocket.client;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.env.EnvUtil;
import com.binance.platform.websocket.WebSocketProperties;
import com.binance.platform.websocket.support.WebSocket;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

@Sharable
public class WebSocketClient extends ChannelInboundHandlerAdapter implements WebSocket, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClient.class);
    private static final String NETTY_CLIENT_NAME_DEFAULT = "Netty_WebSocket_Client";
    private final WebSocketProperties properties;
    private ScheduledExecutorService timer;
    private Channel channel;
    private EventLoopGroup workerGroup;
    private KeepAliveTask keepAliveThread = null;
    private final Counter connects;
    private final Counter disconnects;
    private final Counter messages;

    public WebSocketClient(WebSocketProperties properties) {
        this.properties = properties;
        this.connects = Metrics.globalRegistry.counter("websocket.connect");
        this.disconnects = Metrics.globalRegistry.counter("websocket.disconnect");
        this.messages = Metrics.globalRegistry.counter("websocket.message.received");
    }

    @Override
    public void start() {
        try {
            String url = this.properties.getClient().getUrl();
            URI uri = new URI(url);
            WebSocketClientInboundHandler webSocketClientHandler =
                new WebSocketClientInboundHandler(WebSocketClientHandshakerFactory.newHandshaker(uri,
                    WebSocketVersion.V13, null, true, HttpHeaders.EMPTY_HEADERS, 100 * 1024 * 1024), messages);
            EventLoopGroup worker = new NioEventLoopGroup(properties.getClient().getWorkThreads(),
                new CategorizedThreadFactory(NETTY_CLIENT_NAME_DEFAULT, "Work"));
            Bootstrap connectionBootstrap = new Bootstrap();
            connectionBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
            connectionBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            connectionBootstrap.option(ChannelOption.TCP_NODELAY, true);
            connectionBootstrap.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true));
            connectionBootstrap.group(worker).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        if (isSsl()) {
                            SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
                            sslEngine.setUseClientMode(true);
                            pipeline.addLast(new SslHandler(sslEngine));
                        }
                        pipeline.addLast(new HttpContentDecompressor());
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        pipeline.addLast(WebSocketClient.this);
                        pipeline.addLast(webSocketClientHandler);
                    }

                });

            int port = uri.getPort();
            if (port == -1) {
                if (isSsl()) {
                    port = 443;
                } else {
                    port = 80;
                }
            }
            ChannelFuture channelFuture = connectionBootstrap.connect(uri.getHost(), port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        LOGGER.info("WebSocket Connect Success：{}!", uri);
                    }
                }
            });
            Channel channel = channelFuture.syncUninterruptibly().channel();
            this.keepAliveThread = new KeepAliveTask(channel);
            new Thread(keepAliveThread).start();
            this.channel = channel;
            this.workerGroup = worker;
            this.resetReconnectTimer();
            connects.increment();
        } catch (Throwable e) {
            LOGGER.error("connect wss server failed", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (keepAliveThread != null) {
                keepAliveThread.stop();
            }
            if (channel != null && channel.isOpen()) {
                channel.writeAndFlush(new CloseWebSocketFrame());
                channel.closeFuture().sync();
            }
            this.workerGroup.shutdownGracefully();
        } catch (Throwable e) {
            LOGGER.error("gracefully stop failed", e);
        }
    }

    public Future<Void> sendTextMessage(String message) {
        TextWebSocketFrame text = new TextWebSocketFrame(message);
        return channel.writeAndFlush(text);
    }

    public Future<Void> sendBinaryMessage(byte[] message) {
        ByteBuf byteBuf = Unpooled.buffer(message.length).writeBytes(message);
        BinaryWebSocketFrame binary = new BinaryWebSocketFrame(byteBuf);
        return channel.writeAndFlush(binary);
    }

    public Future<Void> sendFrame(WebSocketFrame frame) {
        return channel.writeAndFlush(frame);
    }

    public boolean isOpen() {
        return channel != null && channel.isActive();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        disconnects.increment();
        this.timer.scheduleAtFixedRate(this, 0, 30, TimeUnit.SECONDS);
        this.workerGroup.shutdownGracefully();
        if (keepAliveThread != null) {
            keepAliveThread.stop();
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void run() {
        if (EnvUtil.isNotMacOs()) {
            boolean isOpen = this.isOpen();
            if (!isOpen) {
                LOGGER.info("Begin Reconnect WebSocket!");
                start();
            }
        }
    }

    private void resetReconnectTimer() {
        if (this.timer != null) {
            this.timer.shutdown();
            this.timer = null;
        }
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    private boolean isSsl() {
        boolean isSsl = false;
        if (properties.getClient().getUrl().startsWith("wss")) {
            isSsl = true;
        }
        return isSsl;
    }

}
