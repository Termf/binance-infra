package com.binance.platform.websocket.server.cluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.binance.platform.websocket.support.WebSocketSession;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.net.NetUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class WebSocketChannelRepository extends WebSocketCluster {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketChannelRepository.class);

	// 在redis保存的集群列表
	private static final String REDIS_WEBSOCKET_SERVER = "WebSocket_Server";

	// 在redis保存的客户端Session列表
	private static final String REDIS_WEBSOCKET_CLIENT_PRIFIX = "WebSocket_Server_Client_";

	// 本地保存的客户端
	private static final ChannelGroup SERVER_ALLCHANNELS = new DefaultChannelGroup("Netty_WebSocket_Server",
			GlobalEventExecutor.INSTANCE);

	private static final Map<String, ChannelId> SERVER_ALLCHANNELIDS = Maps.newConcurrentMap();

	private RedisTemplate redisTemplate;

	private static class SingletonHoler {
		private static WebSocketChannelRepository instance = new WebSocketChannelRepository();
	}

	private WebSocketChannelRepository() {
		this.redisTemplate = super.getRedisTemplate();
	}

	public static WebSocketChannelRepository getInstance() {
		return SingletonHoler.instance;
	}

	/**
	 * 删除集群节点及所有的Channel
	 */
	public void deleteClusterAndChannel() {
		String webSocketServerAddress = LOCAL_WEBSOCKET_ADDRESS;
		String webSocketClientKey = REDIS_WEBSOCKET_CLIENT_PRIFIX + webSocketServerAddress;
		redisTemplate.delete(webSocketClientKey);
	}

	/**
	 * 保存Channel到本地，并且把SessionId保存到Redis
	 */
	public void saveChannelToLocalAndRemote(Channel channel) {
		// 把ChannelId保存起来，方便查找
		String sessionId = channel.attr(WebSocketSession.SESSION_ID).get();
		SERVER_ALLCHANNELIDS.put(sessionId, channel.id());
		// 把Chanel保存起来
		SERVER_ALLCHANNELS.add(channel);
		if (redisTemplate != null) {
			String webSocketServerAddress = LOCAL_WEBSOCKET_ADDRESS;
			// 首先存放所有的WebSocket服务列表
			BoundSetOperations clusterOperations = redisTemplate.boundSetOps(REDIS_WEBSOCKET_SERVER);
			clusterOperations.add(webSocketServerAddress);
			LOGGER.info("All Cluster:" + StringUtils.join(clusterOperations.members(), ","));
			// 其次存放服务列表下的Channel
			String webSocketClientKey = REDIS_WEBSOCKET_CLIENT_PRIFIX + webSocketServerAddress;
			// 缓存当前服务端节点下连了那些客户端
			BoundSetOperations channelOperations = redisTemplate.boundSetOps(webSocketClientKey);
			channelOperations.add(sessionId);
			// 当连接中断时添加监听事件，把redis的sessionId清理掉，并把本地内存的ChannelID清理掉
			channel.closeFuture().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// 当Channel断连时，把ChannelId也从Redis中删除
					String sessionId = future.channel().attr(WebSocketSession.SESSION_ID).get();
					redisTemplate.opsForSet().remove(webSocketClientKey, sessionId);
					SERVER_ALLCHANNELIDS.remove(sessionId);
				}

			});
		}
	}

	/**
	 * 查找本地的Channel
	 */
	public Channel findLocalChannel(String sessionId) {
		ChannelId channelId = SERVER_ALLCHANNELIDS.get(sessionId);
		if (channelId != null) {
			return SERVER_ALLCHANNELS.find(channelId);
		}
		return null;
	}

	/**
	 * 查找本地所有的Channel
	 */
	public List<Channel> findAllLocalChannel() {
		return Lists.newArrayList(SERVER_ALLCHANNELS.iterator());
	}

	/**
	 * 查找本地保存的ChannelGroup
	 */
	public ChannelGroup findLocalChannelGroup() {
		return SERVER_ALLCHANNELS;
	}

	/**
	 * 查找远程的channel所在的服务器节点地址
	 */
	public String findRemoteChannelAddress(String sessionId) {
		if (redisTemplate != null) {
			Set<String> allWebSocketServers = redisTemplate.opsForSet().members(REDIS_WEBSOCKET_SERVER);
			String returnwebSocketServerAddress = null;
			for (String webSocketServerAddress : allWebSocketServers) {
				String webSocketClientKey = REDIS_WEBSOCKET_CLIENT_PRIFIX + webSocketServerAddress;
				Set<String> sessionIds = redisTemplate.opsForSet().members(webSocketClientKey);
				if (sessionIds.contains(sessionId)) {
					returnwebSocketServerAddress = webSocketServerAddress;
					break;
				}
			}
			return returnwebSocketServerAddress;
		}
		return NetUtil.getLocalHost();
	}

	/**
	 * 查找所有的集群的其他节点
	 */
	public List<String> findAllRemoteAddress() {
		if (redisTemplate != null) {
			Set<String> allWebSocketServers = redisTemplate.opsForSet().members(REDIS_WEBSOCKET_SERVER);
			return Lists.newArrayList(allWebSocketServers);
		} else {
			return Lists.newArrayList();
		}
	}

}
