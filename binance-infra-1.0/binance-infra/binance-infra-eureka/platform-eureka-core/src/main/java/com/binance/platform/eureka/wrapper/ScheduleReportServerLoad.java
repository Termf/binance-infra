
package com.binance.platform.eureka.wrapper;

import static com.binance.platform.EurekaConstants.EUREKA_METADATA_ENVKEY;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GRAYFLOW;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_GROUP;
import static com.binance.platform.EurekaConstants.EUREKA_METADATA_VERSION;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_ENVKEY;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GRAYFLOW;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_GROUP;
import static com.binance.platform.EurekaConstants.PROVIDER_INSTANCE_VERSION;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import com.binance.platform.EurekaConstants;
import com.binance.platform.MyJsonUtil;
import com.binance.platform.ServerLoadStatus;
import com.netflix.appinfo.ApplicationInfoManager;

public class ScheduleReportServerLoad {

    private static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        private final String mPrefix;

        private final boolean mDaemon;

        private final ThreadGroup mGroup;

        public NamedThreadFactory() {
            this("ScheduleCache-" + POOL_SEQ.getAndIncrement(), true);
        }

        public NamedThreadFactory(String prefix, boolean daemon) {
            mPrefix = prefix + "-thread-";
            mDaemon = daemon;
            SecurityManager s = System.getSecurityManager();
            mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String name = mPrefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(mGroup, runnable, name, 0);
            ret.setDaemon(mDaemon);
            return ret;
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleReportServerLoad.class);

    final EurekaRegistration registration;
    final ConfigurableEnvironment environment;
    final String sourceEnvKey;

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
        if (grayflow != null) {
            map.put(EUREKA_METADATA_GRAYFLOW, grayflow);
        } else {
            map.remove(EUREKA_METADATA_GRAYFLOW);
        }

    }

    public void start() {
        ScheduledExecutorService scheduleReport = Executors.newScheduledThreadPool(1, new NamedThreadFactory());
        scheduleReport.scheduleAtFixedRate(new Runnable() {

            @Override
            @SuppressWarnings("deprecation")
            public void run() {
                try {
                    Map<String, String> map = ApplicationInfoManager.getInstance().getInfo().getMetadata();
                    ServerLoadStatus serverLoadStatus = new ServerLoadStatus();
                    serverLoadStatus.calculateSystemInfo();
                    String serverLoadStatusJson = MyJsonUtil.toJson(serverLoadStatus);
                    map.put(EurekaConstants.EUREKA_METADATA_SERVERLOAD, serverLoadStatusJson);
                    Assert.notNull(registration, "registration MUST NOT BE NULL");
                    Assert.notNull(registration.getMetadata(), "registration.getMetadata() MUST NOT BE NULL");
                    Assert.notNull(ApplicationInfoManager.getInstance(),
                        "ApplicationInfoManager.getInstance() MUST NOT BE NULL");
                    Assert.notNull(ApplicationInfoManager.getInstance().getInfo(),
                        "ApplicationInfoManager.getInstance().getInfo() MUST NOT BE NULL");
                    Assert.notNull(ApplicationInfoManager.getInstance().getInfo().getMetadata(),
                        "ApplicationInfoManager.getInstance().getInfo().getMetadata() MUST NOT BE NULL");
                    map.putAll(registration.getMetadata());
                    registerAgain(map);
                    ApplicationInfoManager.getInstance().registerAppMetadata(map);
                } catch (Throwable e) {
                    LOGGER.warn(e.getMessage());
                }

            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}
