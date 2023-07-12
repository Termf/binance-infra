package com.binance.platform.monitor.endpoint;

import com.binance.master.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;


@RestControllerEndpoint(id = "clearCache")
@Slf4j
public class JetCacheManagerEndpoint {

    @Autowired(required = false)
    @Qualifier("redisConnectionFactory")
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    protected ConfigurableEnvironment environment;

    public static final String REMOTE_DEFAULT_KEY_PREFIX = "jetcache.";

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String clearCache(HttpServletRequest request) {
        if (redisConnectionFactory != null) {
            String keyPrefix =
                    new StringBuilder(REMOTE_DEFAULT_KEY_PREFIX).append(environment.getProperty("spring.application.name")).append(".").toString();
            String cacheName = request.getParameter("cacheName");
            if (StringUtils.isBlank(cacheName)) {
                return "fail";
            }
            if (StringUtils.equalsIgnoreCase("all", cacheName)) {
                scanAndDel(keyPrefix);
            } else {
                scanAndDel(keyPrefix + cacheName);
            }
            return "success";
        }
        return "fail";
    }

    private void scanAndDel(String matchKey) {
        RedisClusterConnection redisClusterConnection = redisConnectionFactory.getClusterConnection();
        Iterator<RedisClusterNode> redisClusterNodes = redisClusterConnection.clusterGetNodes().iterator();
        while (redisClusterNodes.hasNext()) {
            RedisClusterNode redisNode = redisClusterNodes.next();
            Cursor<byte[]> keyCursor =
                    redisClusterConnection.scan(redisNode, new ScanOptions.ScanOptionsBuilder().match(matchKey + "*").count(1000).build());
            List<byte[]> keys = Lists.newArrayList();
            while (keyCursor.hasNext()) {
                keys.add(keyCursor.next());
            }
            byte[][] keysArray = new byte[keys.size()][];
            for (int i = 0; i < keysArray.length; i++) {
                keysArray[i] = keys.get(i);
            }
            if (keys.size() > 0) {
                redisClusterConnection.del(keysArray);
            }
        }
        redisClusterConnection.close();
    }

}
