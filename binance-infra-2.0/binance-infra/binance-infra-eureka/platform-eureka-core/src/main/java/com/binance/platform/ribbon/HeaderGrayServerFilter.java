package com.binance.platform.ribbon;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.binance.platform.EurekaConstants;
import com.binance.platform.common.RpcContext;
import com.binance.platform.env.EnvUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * 由Header头指定灰度规则
 */
public class HeaderGrayServerFilter extends DefaultGrayServerFilter {

	public static final Logger logger = LoggerFactory.getLogger(HeaderGrayServerFilter.class);

	public static final String GRAY_ENV_HEADER = "X-GRAY-ENV";

	public HeaderGrayServerFilter(ConfigurableEnvironment environment) {
		super(environment);
	}

	protected void doMatchEnvKey(final Set<Server> filteredServer, final DiscoveryEnabledServer server,
			final String envKey) {
		final Map<String, String> instanceMeta = server.getInstanceInfo().getMetadata();
		String envKeyMeta = instanceMeta.get(EurekaConstants.EUREKA_METADATA_ENVKEY);
		boolean envKeyEqual = StringUtils.equals(envKeyMeta, envKey);
		if (envKeyEqual) {
			filteredServer.add(server);
		}
	}

	@Override
	public List<Server> match(List<Server> servers) {
		if (servers == null || servers.size() == 0) {
			return servers;
		}
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			String envKey = null;
			if (requestAttributes != null) {
				HttpServletRequest request = requestAttributes.getRequest();
				envKey = request.getHeader(GRAY_ENV_HEADER);
			}
			if (envKey == null) {
				envKey = RpcContext.getContext().get(GRAY_ENV_HEADER);
			}
			if (envKey != null) {
				Set<Server> filteredServer = Sets.newHashSet();
				for (Server insaceInfo : servers) {
					if (insaceInfo instanceof DiscoveryEnabledServer) {
						DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) insaceInfo;
						doMatchEnvKey(filteredServer, discoveryEnabledServer, envKey);
					}
					// 这个是走不到的
					else {
						filteredServer.add(insaceInfo);
					}
				}
				// 如果真实匹配到了，就返回回去
				if (filteredServer.size() != 0) {
					if (!EnvUtil.isProd()) {
						logger.info(String.format(
								"gray rule %s is effect,the original server list is:%s ,the filter server list is:%s",
								envKey, servers.stream().map(Server::getHost).collect(Collectors.joining(",")),
								filteredServer.stream().map(Server::getHost).collect(Collectors.joining(","))));
					}
					return super.warmUp(Lists.newArrayList(filteredServer));
				}
				// 如果没有匹配上，调用下父类的匹配规则，尽量让用户匹配上
				else {
					// 如果非生产环境，把本地IP剔除掉
					if (!EnvUtil.isProd()) {
						deleteLocalIp(servers);
					}
					List<Server> serversList = super.match(servers);
					if (!EnvUtil.isProd()) {
						logger.info(String.format("gray rule %s is not effect,will forward to server list is %s",
								envKey, serversList.stream().map(Server::getHost).collect(Collectors.joining(","))));
					}
					return serversList;
				}
			}
			return super.match(servers);
		} finally {
			RpcContext.getContext().remove(GRAY_ENV_HEADER);
		}

	}

	private void deleteLocalIp(List<Server> servers) {
		Iterator<Server> it = servers.iterator();
		while (it.hasNext()) {
			Server server = it.next();
			String localIp = server.getHost();
			if (StringUtils.startsWithIgnoreCase(localIp, "192.168")
					|| StringUtils.startsWithIgnoreCase(localIp, "10.198")) {
				it.remove();
			}
		}
	}

}
