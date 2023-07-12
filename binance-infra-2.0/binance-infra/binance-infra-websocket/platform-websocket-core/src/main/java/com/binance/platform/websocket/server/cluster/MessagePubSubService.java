package com.binance.platform.websocket.server.cluster;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

@SuppressWarnings(value = {"rawtypes"})
public class MessagePubSubService extends WebSocketCluster implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePubSubService.class);

    private RedisTemplate redisTemplate;

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public MessagePubSubService() {
        this.redisTemplate = super.getRedisTemplate();
        Thread subscribeTask = new Thread() {
            @Override
            public void run() {
                // 只订阅本机IP的消息
                MessagePubSubService.this.redisTemplate.getConnectionFactory().getConnection()
                    .subscribe(MessagePubSubService.this, LOCAL_WEBSOCKET_ADDRESS.getBytes());
            }
        };
        subscribeTask.start();
    }

    /**
     * 推送文本消息给远程的redis节点
     */
    public void pubTextMessageToRemoteAddress(String remoteAddress, String toSessionId, String message) {
        try {
            if (StringUtils.startsWithIgnoreCase(LOCAL_WEBSOCKET_ADDRESS, remoteAddress)) {
                return;
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage(toSessionId, message);
            String json = OBJECTMAPPER.writeValueAsString(webSocketMessage);
            redisTemplate.getConnectionFactory().getConnection().publish(remoteAddress.getBytes(CharsetUtil.UTF_8),
                json.getBytes());
        } catch (Throwable e) {
            LOGGER.warn("cant not pubMessageToClusterNode" + e.getMessage());
        }
    }

    /**
     * 推送字节消息给远程的redis节点
     */
    public void pubByteMessageToRemoteAddress(String remoteAddress, String toSessionId, byte[] message) {
        try {
            if (StringUtils.startsWithIgnoreCase(LOCAL_WEBSOCKET_ADDRESS, remoteAddress)) {
                return;
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage(toSessionId, message);
            String json = OBJECTMAPPER.writeValueAsString(webSocketMessage);
            redisTemplate.getConnectionFactory().getConnection().publish(remoteAddress.getBytes(CharsetUtil.UTF_8),
                json.getBytes());

        } catch (Throwable e) {
            LOGGER.warn("cant not pubMessageToClusterNode" + e.getMessage());
        }
    }

    /**
     * 推送文本消息给远程的redis节点
     */
    public void pubBroadcastTextMessageToRemoteAddress(String remoteAddress, String message) {
        try {
            if (StringUtils.startsWithIgnoreCase(LOCAL_WEBSOCKET_ADDRESS, remoteAddress)) {
                return;
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage(Boolean.TRUE, message);
            String json = OBJECTMAPPER.writeValueAsString(webSocketMessage);
            redisTemplate.getConnectionFactory().getConnection().publish(remoteAddress.getBytes(CharsetUtil.UTF_8),
                json.getBytes());
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.warn("cant not pubMessageToClusterNode" + e.getMessage());
        }
    }

    /**
     * 推送字节消息给远程的redis节点
     */
    public void pubBroadcastByteMessageToRemoteAddress(String remoteAddress, byte[] message) {
        try {
            if (StringUtils.startsWithIgnoreCase(LOCAL_WEBSOCKET_ADDRESS, remoteAddress)) {
                return;
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage(Boolean.TRUE, message);
            String json = OBJECTMAPPER.writeValueAsString(webSocketMessage);
            redisTemplate.getConnectionFactory().getConnection().publish(remoteAddress.getBytes(CharsetUtil.UTF_8),
                json.getBytes());
        } catch (Throwable e) {
            LOGGER.warn("cant not pubMessageToClusterNode" + e.getMessage());
        }
    }

    @Override
    public void onMessage(Message redisMessage, byte[] pattern) {
        try {
            byte[] body = redisMessage.getBody();
            WebSocketMessage webSocketMessage = OBJECTMAPPER.readValue(new String(body), WebSocketMessage.class);
            String toSessionId = webSocketMessage.getToSessionId();
            String textMessage = webSocketMessage.getTextMessage();
            byte[] binaryMessage = webSocketMessage.getBinaryMessage();
            Boolean broadcast = webSocketMessage.getBroadcast();
            // 如果点对点
            if (toSessionId != null) {
                Channel channel = WebSocketChannelRepository.getInstance().findLocalChannel(toSessionId);
                if (channel != null) {
                    if (textMessage != null) {
                        channel.writeAndFlush(new TextWebSocketFrame(textMessage));
                    }
                    if (binaryMessage != null) {
                        ByteBuf buffer = channel.alloc().buffer(binaryMessage.length);
                        channel.writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(binaryMessage)));
                    }
                }
            }
            // 如果广播
            if (broadcast) {
                List<Channel> channelList = WebSocketChannelRepository.getInstance().findAllLocalChannel();
                for (Channel channel : channelList) {
                    if (channel != null) {
                        if (textMessage != null) {
                            channel.writeAndFlush(new TextWebSocketFrame(textMessage));
                        }
                        if (binaryMessage != null) {
                            ByteBuf buffer = channel.alloc().buffer(binaryMessage.length);
                            channel.writeAndFlush(new BinaryWebSocketFrame(buffer.writeBytes(binaryMessage)));
                        }
                    }
                }

            }

        } catch (Throwable e) {
            LOGGER.warn("cant not subMessageToClusterNode" + e.getMessage());
        }

    }

}
