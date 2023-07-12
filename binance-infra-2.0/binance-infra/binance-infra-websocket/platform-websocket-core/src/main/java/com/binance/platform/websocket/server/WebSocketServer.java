package com.binance.platform.websocket.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.websocket.WebSocketProperties;
import com.binance.platform.websocket.server.cluster.WebSocketChannelRepository;
import com.binance.platform.websocket.support.WebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class WebSocketServer implements WebSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    private static final String NETTY_SERVER_NAME_DEFAULT = "Netty_WebSocket_Server";

    private static final AtomicBoolean SERVER_STOPED = new AtomicBoolean(false);

    private final Thread jvmShutdownHook = new Thread(new Runnable() {
        @Override
        public void run() {
            stop();
        }
    }, NETTY_SERVER_NAME_DEFAULT + "_Hook");

    private final WebSocketProperties properties;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public WebSocketServer(WebSocketProperties properties) {
        this.properties = properties;
    }

    /**
     * 启动Netty Server
     */
    @Override
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(properties.getServer().getBossThreads(),
            new CategorizedThreadFactory(NETTY_SERVER_NAME_DEFAULT, "Boss"));
        EventLoopGroup worker = new NioEventLoopGroup(properties.getServer().getWorkThreads(),
            new CategorizedThreadFactory(NETTY_SERVER_NAME_DEFAULT, "Work"));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)//
            .option(ChannelOption.SO_BACKLOG, 128)//
            .option(ChannelOption.SO_LINGER, -1)//
            .option(ChannelOption.TCP_NODELAY, true)//
            .option(ChannelOption.SO_KEEPALIVE, true)//
            .childOption(ChannelOption.WRITE_SPIN_COUNT, 16)
            .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(8 * 1024, 32 * 1024))
            .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false).handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    pipeline.addLast(new HttpServerInboundHandler(properties));
                }
            });
        ChannelFuture channelFuture;
        if ("0.0.0.0".equals(properties.getServer().getHost())) {
            channelFuture = bootstrap.bind(properties.getServer().getPort());
        } else {
            try {
                channelFuture =
                    bootstrap.bind(new InetSocketAddress(InetAddress.getByName(properties.getServer().getHost()),
                        properties.getServer().getPort()));
            } catch (UnknownHostException e) {
                channelFuture = bootstrap.bind(properties.getServer().getHost(), properties.getServer().getPort());
                LOGGER.error(e.getMessage(), e);
            }
        }
        channelFuture.addListener(future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
            }
        });
        this.bossGroup = boss;
        this.workerGroup = worker;
        Runtime.getRuntime().addShutdownHook(jvmShutdownHook);
        LOGGER.info("####################################################################################");
        LOGGER.info("#");
        LOGGER.info("WebSocket Server started at address: " + properties.getServer().getPort());
        LOGGER.info("#");
        LOGGER.info("####################################################################################");
    }

    /**
     * 优雅关闭NettyServer
     */
    @Override
    public void stop() {
        if (SERVER_STOPED.compareAndSet(false, true)) {
            WebSocketChannelRepository webSocketChannelRepository = WebSocketChannelRepository.getInstance();
            try {
                webSocketChannelRepository.deleteClusterAndChannel();
            } catch (Throwable e) {
            }
            try {
                TimeUnit.SECONDS.sleep(10);
                ChannelGroup channelGroup = webSocketChannelRepository.findLocalChannelGroup();
                channelGroup.writeAndFlush(new CloseWebSocketFrame());
                ChannelGroupFuture future = channelGroup.close();
                future.await(30, TimeUnit.SECONDS);
                if (!future.isSuccess()) {
                    for (ChannelFuture cf : future) {
                        if (!cf.isSuccess()) {
                            LOGGER.info("Unable to close channel.  Cause of failure for {} is {}", cf.channel(),
                                cf.cause());
                        }
                    }
                }
                this.bossGroup.shutdownGracefully().syncUninterruptibly();
                this.workerGroup.shutdownGracefully().syncUninterruptibly();
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }

        }
    }

}
