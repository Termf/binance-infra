
package com.binance.platform.eureka.wrapper;

import static com.binance.platform.EurekaConstants.EUREKA_METADATA_ENVKEY;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GRAYFLOW;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GROUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_VERSION;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_WARMUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_WARMUP_DELAY_TIME;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_ENVKEY;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GRAYFLOW;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GROUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_VERSION;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_WARMUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_WARMUP_DELAY_TIME;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.EurekaConstants;
import com.binance.platform.MyJsonUtil;
import com.binance.platform.ServerLoadStatus;
import com.netflix.appinfo.ApplicationInfoManager;

@SuppressWarnings("deprecation")
public class ScheduleReportServerLoad {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleReportServerLoad.class);

    private final EurekaRegistration registration;
    private final ConfigurableEnvironment environment;
    private final String sourceEnvKey;
    private final Random random = new Random();
    private final AtomicReference<Map<String, String>> metadataSnapshot =
        new AtomicReference<Map<String, String>>(null);
    private volatile long lastModifiedTime = 0L;

    public ScheduleReportServerLoad(EurekaRegistration registration, ConfigurableEnvironment environment) {
        this.registration = registration;
        this.environment = environment;
        this.sourceEnvKey = this.getProperties("eureka.instance.metadataMap.envflag") != null
            ? this.getProperties("eureka.instance.metadataMap.envflag") : this.getProperties(PROVIDER_INSTANCE_ENVKEY);
    }

    private String getProperties(String key) {
        return this.environment.getProperty(key);
    }

    public void registerAgain(Map<String, String> map) {
        String group = this.getProperties(PROVIDER_INSTANCE_GROUP);
        String version = this.getProperties(PROVIDER_INSTANCE_VERSION);
        String envKey = this.getProperties(PROVIDER_INSTANCE_ENVKEY);
        String grayflow = this.getProperties(PROVIDER_INSTANCE_GRAYFLOW);
        String warmup = this.getProperties(PROVIDER_INSTANCE_WARMUP);
        String warmupDelayTime = this.getProperties(PROVIDER_INSTANCE_WARMUP_DELAY_TIME);
        if (group != null && version != null) {
            map.put(EUREKA_METADATA_GROUP, group);
            map.put(EUREKA_METADATA_VERSION, version);
        } else {
            map.remove(EUREKA_METADATA_GROUP);
            map.remove(EUREKA_METADATA_VERSION);
        }
        if (envKey != null) {
            map.put(EUREKA_METADATA_ENVKEY, envKey);
        } else if (sourceEnvKey != null) {
            map.put(EUREKA_METADATA_ENVKEY, sourceEnvKey);
        } else {
            map.remove(EUREKA_METADATA_ENVKEY);
        }
        if (warmup != null) {
            map.put(EUREKA_METADATA_WARMUP, warmup);
        } else {
            map.remove(EUREKA_METADATA_WARMUP);
        }
        if (warmupDelayTime != null) {
            map.put(EUREKA_METADATA_WARMUP_DELAY_TIME, warmupDelayTime);
        } else {
            map.remove(EUREKA_METADATA_WARMUP_DELAY_TIME);
        }
        if (grayflow != null) {
            map.put(EUREKA_METADATA_GRAYFLOW, grayflow);
        } else {
            map.remove(EUREKA_METADATA_GRAYFLOW);
        }

    }

    public void start() {
        ScheduledExecutorService scheduleReport = Executors.newScheduledThreadPool(1);
        scheduleReport.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = ApplicationInfoManager.getInstance().getInfo().getMetadata();
                    ServerLoadStatus serverLoadStatus = new ServerLoadStatus();
                    serverLoadStatus.calculateSystemInfo();
                    String serverLoadStatusJson = MyJsonUtil.toJson(serverLoadStatus);
                    map.put(EurekaConstants.EUREKA_METADATA_SERVERLOAD, serverLoadStatusJson);
                    map.putAll(registration.getMetadata());
                    registerAgain(map);
                    if (hasMetadataChanged(map, serverLoadStatus)) {
                        ApplicationInfoManager.getInstance().registerAppMetadata(map);
                    }
                } catch (Throwable e) {
                    metadataSnapshot.set(null);
                    LOGGER.warn(e.getMessage());
                }

            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    private boolean hasMetadataChanged(Map<String, String> currentMap, ServerLoadStatus currentLoadStatus) {
        try {
            Map<String, String> mapSnapshot = metadataSnapshot.get();
            if (MapUtils.isEmpty(mapSnapshot)) {
                saveSnapshot(currentMap);
                return true;
            }
            long maxWaitLimit = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(120 + random.nextInt(40));
            if (lastModifiedTime <= maxWaitLimit) {
                saveSnapshot(currentMap);
                return true;
            }
            // 比较 serverLoad，当前只要busy thread超过25% 就会上报
            if (currentLoadStatus.getCurrentThreadsBusy() >= currentLoadStatus.getMaxThreads() * 0.25) {
                metadataSnapshot.set(currentMap);
                return true;
            }
            // 比较属性
            if (!StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_GROUP), currentMap.get(EUREKA_METADATA_GROUP))
                || !StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_VERSION),
                    currentMap.get(EUREKA_METADATA_VERSION))
                || !StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_ENVKEY), currentMap.get(EUREKA_METADATA_ENVKEY))
                || !StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_WARMUP), currentMap.get(EUREKA_METADATA_WARMUP))
                || !StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_WARMUP_DELAY_TIME),
                    currentMap.get(EUREKA_METADATA_WARMUP_DELAY_TIME))
                || !StringUtils.equals(mapSnapshot.get(EUREKA_METADATA_GRAYFLOW),
                    currentMap.get(EUREKA_METADATA_GRAYFLOW))) {
                saveSnapshot(currentMap);
                return true;
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("compare eureka instance server load error", e);
            saveSnapshot(currentMap);
            return true;
        }
    }

    private void saveSnapshot(Map<String, String> currentMap) {
        metadataSnapshot.set(new HashMap<>(currentMap));
        lastModifiedTime = System.currentTimeMillis();
    }

}
