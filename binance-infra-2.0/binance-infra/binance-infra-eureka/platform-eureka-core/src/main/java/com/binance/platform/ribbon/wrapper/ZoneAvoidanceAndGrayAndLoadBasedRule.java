
package com.binance.platform.ribbon.wrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.ribbon.ServerFilter;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

public class ZoneAvoidanceAndGrayAndLoadBasedRule extends ZoneAvoidanceRule {

	public static final Logger logger = LoggerFactory.getLogger(ZoneAvoidanceAndGrayAndLoadBasedRule.class);

	@Autowired
	private ServerFilter serverFilter;

	@Autowired
	private ApplicationContext applicationContext;

	private AtomicInteger refreshRegistryCount = new AtomicInteger(0);

	@Override
	public Server choose(Object key) {
		List<Server> reachableServerList = getLoadBalancer().getReachableServers();
		if (reachableServerList != null && reachableServerList.size() > 0) {
			List<Server> serverList = new ArrayList<>();
			serverList.addAll(reachableServerList);
			serverList = serverFilter.match(serverList);
			if (serverList == null || serverList.size() == 0) {
				return null;
			}
			Server server = this.chooseInner(serverList, key);
			List<String> results = serverList.stream().map(result -> result.getHostPort()).collect(Collectors.toList());
			logger.debug("Choose the node: {}.All Servers: {}", server.getHostPort(), StringUtils.join(results, ","));
			return server;
		} else {
			try {
				CloudEurekaClient eurekaClient = applicationContext.getBean(CloudEurekaClient.class);
				if (eurekaClient instanceof CloudEurekaClient) {
					// 如果获取的服务列表为空,强制刷新下服务发现的缓存
					logger.error("can not found any node, will fetch registry from server immediately");
					Method refreshRegistry = ReflectionUtils.findMethod(
							org.springframework.cloud.netflix.eureka.CloudEurekaClient.class, "refreshRegistry");
					if (refreshRegistry != null) {
						ReflectionUtils.makeAccessible(refreshRegistry);
						ReflectionUtils.invokeMethod(refreshRegistry, eurekaClient);
					}
					// 手动调用一下DynamicServerListLoadBalancer的updateListOfServers，防止由于PollingServerListUpdater定时任务的原因没能及时更新upServerList
					ILoadBalancer loadBalancer = getLoadBalancer();
					if (loadBalancer instanceof DynamicServerListLoadBalancer) {
						Method updateListOfServers = ReflectionUtils.findMethod(
								com.netflix.loadbalancer.DynamicServerListLoadBalancer.class, "updateListOfServers");
						if (updateListOfServers != null) {
							ReflectionUtils.makeAccessible(updateListOfServers);
							ReflectionUtils.invokeMethod(updateListOfServers, loadBalancer);
						}
					}
					refreshRegistryCount.incrementAndGet();
					// 如果强制刷新3次还找不到，则说明服务的确没部署，返回null出去
					if (refreshRegistryCount.get() > 3) {
						return null;
					}
					// 否则递归走Ribbon负载均衡
					else {
						return choose(key);
					}
				}
			} catch (Throwable e) {
				logger.debug(e.getMessage());
			}
			return null;
		}

	}

	protected Server chooseInner(List<Server> reachableServerList, Object key) {
		Server chooseServer = super.getPredicate().chooseRoundRobinAfterFiltering(reachableServerList, key).get();
		return chooseServer;
	}

}
