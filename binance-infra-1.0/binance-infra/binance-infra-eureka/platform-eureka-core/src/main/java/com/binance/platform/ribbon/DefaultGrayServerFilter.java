package com.binance.platform.ribbon;

import static com.binance.platform.EurekaConstants.CONSUMER_INSTANCE_REFERENCE;
import static com.binance.platform.EurekaConstants.CONSUMER_INSTANCE_REFERENCE_ENVKEY;
import static com.binance.platform.EurekaConstants.CONSUMER_INSTANCE_REFERENCE_GROUP;
import static com.binance.platform.EurekaConstants.CONSUMER_INSTANCE_REFERENCE_SERVICEID;
import static com.binance.platform.EurekaConstants.CONSUMER_INSTANCE_REFERENCE_VERSION;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_ENVKEY;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GRAYFLOW;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GROUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_VERSION;
import static com.binance.platform.EurekaConstants.EUREKA_META_ENVKEY_DEFAULT;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.MyJsonUtil;
import com.binance.platform.env.EnvUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

/**
 * 由配置参数指定灰度规则
 */
public class DefaultGrayServerFilter implements ServerFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGrayServerFilter.class);

    private final List<Map<String, String>> SERVICE_REFERENCE_CACHE = Lists.newArrayList();

    // AB测试放百分之10的流量
    private static final String DEFAULT_AB_TEST_PERCENT = "10";

    private Integer LEAST_FLOW_PERCENT = Integer.valueOf(DEFAULT_AB_TEST_PERCENT);

    private final ConfigurableEnvironment environment;

    private final Random LEAST_FLOW_RANDOM = new Random();

    private volatile String reference;

    public DefaultGrayServerFilter(ConfigurableEnvironment environment) {
        this.environment = environment;
        this.initReferenceCache();
    }

    protected void reloadReferenceCache() {
        // 重新加载配置，防止配置变更了
        String reference = environment.getProperty(CONSUMER_INSTANCE_REFERENCE);
        if (this.reference != null && reference != null) {
            if (!this.reference.equals(reference)) {
                this.initReferenceCache();
                this.reference = reference;
            }
        }
    }

    protected void initReferenceCache() {
        String reference = environment.getProperty(CONSUMER_INSTANCE_REFERENCE);
        if (reference != null) {
            List<Map<String, String>> referencList = Lists.newArrayList();
            if (MyJsonUtil.isGoodJson(reference)) {
                referencList = MyJsonUtil.toList(new StringReader(reference));
            } else {
                InputStream in = DefaultGrayServerFilter.class.getClassLoader().getResourceAsStream(reference);
                if (in != null)
                    referencList = MyJsonUtil.toList(new InputStreamReader(in));
            }
            SERVICE_REFERENCE_CACHE.clear();
            SERVICE_REFERENCE_CACHE.addAll(referencList);
            this.reference = reference;
        }
    }

    protected Pair<String, String> getGroupVersion(String serviceId) {
        for (Map<String, String> referenceDefintion : SERVICE_REFERENCE_CACHE) {
            String service = referenceDefintion.get(CONSUMER_INSTANCE_REFERENCE_SERVICEID);
            if (service.toUpperCase().equals(serviceId.toUpperCase())) {
                String group = referenceDefintion.get(CONSUMER_INSTANCE_REFERENCE_GROUP);
                String version = referenceDefintion.get(CONSUMER_INSTANCE_REFERENCE_VERSION);
                return new ImmutablePair<String, String>(group, version);
            }
        }
        return null;
    }

    protected String getEnvKey(String serviceId) {
        for (Map<String, String> referenceDefintion : SERVICE_REFERENCE_CACHE) {
            String service = referenceDefintion.get(CONSUMER_INSTANCE_REFERENCE_SERVICEID);
            if (service.toUpperCase().equals(serviceId.toUpperCase())) {
                String envKey = referenceDefintion.get(CONSUMER_INSTANCE_REFERENCE_ENVKEY);
                return envKey;
            }
        }
        return null;
    }

    protected void doMatchGroupAndVersion(final Set<Server> filteredServer, final DiscoveryEnabledServer server) {
        final String serviceId = server.getInstanceInfo().getAppName();
        final Map<String, String> instanceMeta = server.getInstanceInfo().getMetadata();
        final Pair<String, String> groupVersion = this.getGroupVersion(serviceId);
        String groupMeta = instanceMeta.get(EUREKA_METADATA_GROUP);
        String versionMeta = instanceMeta.get(EUREKA_METADATA_VERSION);
        // 如果有路由规则，则匹配
        if (groupMeta != null && versionMeta != null && groupVersion != null) {
            boolean groupEqual = StringUtils.equals(groupVersion.getLeft(), groupMeta);
            boolean versionEqual = StringUtils.equals(groupVersion.getRight(), versionMeta);
            if (groupEqual && versionEqual) {
                filteredServer.add(server);
            }
        } // 如果节点上打了灰度标签，但是没有传envKey，默认放百分之10的流量
        else if (groupMeta != null && versionMeta != null && groupVersion == null) {
            // 如果是生产，如果节点上打了灰度标签，且打了标签说需要放量，默认放百分之10的流量
            if (EnvUtil.isProd()) {
                boolean grayflow = BooleanUtils.toBoolean(instanceMeta.getOrDefault(EUREKA_METADATA_GRAYFLOW, "true"));
                if (grayflow) {
                    int randomValue = LEAST_FLOW_RANDOM.nextInt(100);
                    if (randomValue <= LEAST_FLOW_PERCENT) {
                        filteredServer.add(server);
                    }
                }
            }
            // 如果是非生产，说明没有指定机器，请求量全加
            else {
                filteredServer.add(server);
            }
        } else {
            filteredServer.add(server);
        }
    }

    protected void doMatchEnvKey(final Set<Server> filteredServer, final DiscoveryEnabledServer server) {
        final String serviceId = server.getInstanceInfo().getAppName();
        final Map<String, String> instanceMeta = server.getInstanceInfo().getMetadata();
        final String envKey = this.getEnvKey(serviceId);
        String envKeyMeta = instanceMeta.get(EUREKA_METADATA_ENVKEY);
        // 如果有路由规则，则匹配
        if (envKeyMeta != null && envKey != null) {
            boolean envKeyEqual = StringUtils.equals(envKeyMeta, envKey);
            if (envKeyEqual) {
                filteredServer.add(server);
            }
        } else if (envKeyMeta != null && envKey == null) {
            // 如果是生产，如果节点上打了normal灰度标签,请求量全加
            if (StringUtils.equalsIgnoreCase(EUREKA_META_ENVKEY_DEFAULT, envKeyMeta)) {
                filteredServer.add(server);
            } else {
                // 如果是生产，如果节点上打了灰度标签，且打了标签说需要放量，默认放百分之10的流量
                if (EnvUtil.isProd()) {
                    boolean grayflow =
                        BooleanUtils.toBoolean(instanceMeta.getOrDefault(EUREKA_METADATA_GRAYFLOW, "true"));
                    if (grayflow) {
                        int randomValue = LEAST_FLOW_RANDOM.nextInt(100);
                        if (randomValue <= LEAST_FLOW_PERCENT) {
                            filteredServer.add(server);
                        }
                    }
                }
            }
        } else {
            filteredServer.add(server);
        }
    }

    @Override
    public List<Server> match(List<Server> servers) {
        this.reloadReferenceCache();
        this.LEAST_FLOW_PERCENT =
            Integer.valueOf(environment.getProperty("com.binance.leastflow.percent", DEFAULT_AB_TEST_PERCENT));
        Set<Server> filteredServer = Sets.newHashSet();
        for (Server insaceInfo : servers) {
            if (insaceInfo instanceof DiscoveryEnabledServer) {
                DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer)insaceInfo;
                // 默认使用envkey来做过滤
                doMatchEnvKey(filteredServer, discoveryEnabledServer);
                // 如果服务端有group和version的标签，过滤一下
                Map<String, String> instanceMeta = discoveryEnabledServer.getInstanceInfo().getMetadata();
                if (instanceMeta.containsKey(EUREKA_METADATA_GROUP)
                    || instanceMeta.containsKey(EUREKA_METADATA_VERSION)) {
                    doMatchGroupAndVersion(filteredServer, discoveryEnabledServer);
                }
            } else {
                filteredServer.add(insaceInfo);
            }
        }
        if (filteredServer.size() != 0) {
            List<Server> newServerList = Lists.newArrayList(filteredServer);
            LOGGER.debug(String.format("the server list is:%s ,the filter server list is:%s",
                servers.stream().map(Server::getHost).collect(Collectors.joining(",")),
                newServerList.stream().map(Server::getHost).collect(Collectors.joining(","))));
            return this.warmUp(newServerList);
        } else {
            LOGGER.debug(String.format("the server list is:%s ,the filter server list is:%s",
                servers.stream().map(Server::getHost).collect(Collectors.joining(",")),
                servers.stream().map(Server::getHost).collect(Collectors.joining(","))));
            return this.warmUp(servers);
        }

    }

    // 开启预热流程
    protected List<Server> warmUp(List<Server> filteredServer) {
        List<Server> warmUpServer = Lists.newArrayList(filteredServer);
        if (warmUpServer.size() > 2) {
            Iterator<Server> it = warmUpServer.iterator();
            while (it.hasNext()) {
                Server server = it.next();
                if (server instanceof DiscoveryEnabledServer) {
                    DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer)server;
                    // 如果服务在eureka上的注册时间少于1分钟，然后取一下随机数，如果随机数大于10，则把该节点从列表里删除
                    if (discoveryEnabledServer.getInstanceInfo().getLeaseInfo().getServiceUpTimestamp() <= 60000) {
                        int randomValue = LEAST_FLOW_RANDOM.nextInt(100);
                        if (randomValue >= LEAST_FLOW_PERCENT) {
                            it.remove();
                        }
                    }
                }
            }
        }
        // 一定要让调用方调用到
        if (warmUpServer.size() == 0) {
            return filteredServer;
        } else {
            return warmUpServer;
        }
    }

}
