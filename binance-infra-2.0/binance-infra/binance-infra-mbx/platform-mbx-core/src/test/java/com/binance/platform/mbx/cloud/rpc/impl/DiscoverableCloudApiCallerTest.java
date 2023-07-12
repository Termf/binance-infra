package com.binance.platform.mbx.cloud.rpc.impl;

import com.binance.platform.EurekaConstants;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/23 10:14 上午
 */
public class DiscoverableCloudApiCallerTest {
    @Test
    public void instanceFilter() {
        DiscoverableCloudApiCaller cloudApiCaller = new DiscoverableCloudApiCaller(null);
        List<ServiceInstance> instanceList = new ArrayList<>();
        Boolean warmUp = null;

        instanceList.add(buildFilterInstance("1", "", 61000, warmUp));
        instanceList.add(buildFilterInstance("2", "normal", 61000, warmUp));
        instanceList.add(buildFilterInstance("3", "normal", 50000, warmUp));
        instanceList.add(buildFilterInstance("4", "dev", 61000, warmUp));
        instanceList.add(buildFilterInstance("5", "192.168.0.0", 61000, warmUp));

        List<ServiceInstance> warmUpInstanceList = cloudApiCaller.filterInstances(instanceList);
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (ServiceInstance serviceInstance : warmUpInstanceList) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(",");
            }
            stringBuilder.append(serviceInstance.getInstanceId());
        }

        Assert.assertEquals("1,2", stringBuilder.toString());
    }

    private ServiceInstance buildFilterInstance(String instanceId, String instanceEnvFlag, long serviceUpTime, Boolean warmUp) {
        HashMap<String, String> metadata = new HashMap<>();
        if (StringUtils.hasText(instanceEnvFlag)) {
            metadata.put(EurekaConstants.EUREKA_METADATA_ENVKEY, instanceEnvFlag);
        }
        if (warmUp != null) {
            metadata.put(EurekaConstants.EUREKA_METADATA_WARMUP, warmUp.toString());
        }

        LeaseInfo leaseInfo = new LeaseInfo(1, 1, 1, 1L,
                1, 1, serviceUpTime);
        InstanceInfo instanceInfo = new InstanceInfo(instanceId, "service-a", "default-group",
                "192.168.1.1", "1", null, null, "homePageUrl",
                "statusPageUrl", "healthCheckUrl", "secureHealthCheck",
                "vipAddress", "secureVipAddress", 1, null,
                "hostName", InstanceInfo.InstanceStatus.UP, InstanceInfo.InstanceStatus.UP,
                InstanceInfo.InstanceStatus.UP, leaseInfo, true, metadata,
                1L, 1L, null, "argName");
        return new EurekaDiscoveryClient.EurekaServiceInstance(instanceInfo);
    }
}
