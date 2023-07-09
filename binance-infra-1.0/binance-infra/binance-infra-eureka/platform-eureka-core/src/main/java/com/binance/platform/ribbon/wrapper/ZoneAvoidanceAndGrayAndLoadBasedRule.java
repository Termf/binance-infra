
package com.binance.platform.ribbon.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.binance.platform.ribbon.ServerFilter;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

public class ZoneAvoidanceAndGrayAndLoadBasedRule extends ZoneAvoidanceRule {

    public static final Logger logger = LoggerFactory.getLogger(ZoneAvoidanceAndGrayAndLoadBasedRule.class);

    @Autowired
    private ServerFilter serverFilter;

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
            return null;
        }
    }

    protected Server chooseInner(List<Server> reachableServerList, Object key) {
        Server chooseServer = super.getPredicate().chooseRoundRobinAfterFiltering(reachableServerList, key).get();
        return chooseServer;
    }

}
