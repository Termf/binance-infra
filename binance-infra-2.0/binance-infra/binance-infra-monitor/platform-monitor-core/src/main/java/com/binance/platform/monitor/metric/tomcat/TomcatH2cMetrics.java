/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.binance.platform.monitor.metric.tomcat;

import com.sun.jmx.interceptor.DefaultMBeanServerInterceptor;
import com.sun.jmx.mbeanserver.JmxMBeanServer;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.lang.NonNullApi;
import io.micrometer.core.lang.NonNullFields;
import io.micrometer.core.lang.Nullable;
import org.apache.catalina.Manager;
import org.apache.coyote.RequestGroupInfo;
import org.apache.tomcat.util.modeler.BaseModelMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * {@link MeterBinder} for Tomcat.
 * <p>Note: the {@link #close()} method should be called when the application shuts down
 * to clean up listeners this binder registers.
 *
 * @author Clint Checketts
 * @author Jon Schneider
 * @author Johnny Lim
 */
@NonNullApi
@NonNullFields
public class TomcatH2cMetrics implements MeterBinder, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(TomcatH2cMetrics.class);
    public static final String TYPE_GLOBAL_REQUEST_PROCESSOR = "GlobalRequestProcessor";
    public static final String KEY_TYPE = "type";
    public static final String OBJECT_NAME_PROPERTY_UPGRADE = "Upgrade";
    public static final String TAG_NAME_PROTOCOL = "protocol";

    private static final String JMX_DOMAIN_EMBEDDED = "Tomcat";
    private static final String JMX_DOMAIN_STANDALONE = "Catalina";
    private static final String OBJECT_NAME_SERVER_SUFFIX = ":type=Server";
    private static final String OBJECT_NAME_SERVER_EMBEDDED = JMX_DOMAIN_EMBEDDED + OBJECT_NAME_SERVER_SUFFIX;
    private static final String OBJECT_NAME_SERVER_STANDALONE = JMX_DOMAIN_STANDALONE + OBJECT_NAME_SERVER_SUFFIX;
    public static final String MBEAN_SCHEMA = "Tomcat";
    public static final String MBEAN_TYPE = "GlobalRequestProcessor";
    public static final String PROTOCOL_H2C = "h2c";

    @Nullable
    private final Manager manager;

    private final MBeanServer mBeanServer;
    private final Iterable<Tag> tags;

    private final Set<NotificationListener> notificationListeners = ConcurrentHashMap.newKeySet();

    private final RequestGroupInfo h2cRequestGroupInfo;

    private volatile String jmxDomain;

    public TomcatH2cMetrics(@Nullable Manager manager, Iterable<Tag> tags, RequestGroupInfo h2cRequestGroupInfo) {
        this(manager, tags, getMBeanServer(), h2cRequestGroupInfo);
    }

    public TomcatH2cMetrics(@Nullable Manager manager, Iterable<Tag> tags, MBeanServer mBeanServer, RequestGroupInfo h2cRequestGroupInfo) {
        this.manager = manager;
        this.tags = tags;
        this.mBeanServer = mBeanServer;
        this.h2cRequestGroupInfo = h2cRequestGroupInfo;
    }

    public static void monitor(MeterRegistry registry, @Nullable Manager manager, RequestGroupInfo h2cRequestGroupInfo, String... tags) {
        monitor(registry, manager, h2cRequestGroupInfo, Tags.of(tags));
    }

    public static void monitor(MeterRegistry registry, @Nullable Manager manager, RequestGroupInfo h2cRequestGroupInfo, Iterable<Tag> tags) {
        new TomcatH2cMetrics(manager, tags, h2cRequestGroupInfo).bindTo(registry);
    }

    public static MBeanServer getMBeanServer() {
        List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (!mBeanServers.isEmpty()) {
            return mBeanServers.get(0);
        }
        return ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        registerGlobalRequestMetrics(registry);
        registerServletMetrics(registry);
        registerCacheMetrics(registry);
        registerThreadPoolMetrics(registry);
        registerSessionMetrics(registry);
    }

    private void registerSessionMetrics(MeterRegistry registry) {
        if (manager == null) {
            // If the binder is created but unable to find the session manager don't register those metrics
            return;
        }

        Gauge.builder("tomcat.sessions.active.max", manager, Manager::getMaxActive)
                .tags(tags)
                .baseUnit(BaseUnits.SESSIONS)
                .register(registry);

        Gauge.builder("tomcat.sessions.active.current", manager, Manager::getActiveSessions)
                .tags(tags)
                .baseUnit(BaseUnits.SESSIONS)
                .register(registry);

        FunctionCounter.builder("tomcat.sessions.created", manager, Manager::getSessionCounter)
                .tags(tags)
                .baseUnit(BaseUnits.SESSIONS)
                .register(registry);

        FunctionCounter.builder("tomcat.sessions.expired", manager, Manager::getExpiredSessions)
                .tags(tags)
                .baseUnit(BaseUnits.SESSIONS)
                .register(registry);

        FunctionCounter.builder("tomcat.sessions.rejected", manager, Manager::getRejectedSessions)
                .tags(tags)
                .baseUnit(BaseUnits.SESSIONS)
                .register(registry);

        TimeGauge.builder("tomcat.sessions.alive.max", manager, TimeUnit.SECONDS, Manager::getSessionMaxAliveTime)
                .tags(tags)
                .register(registry);
    }

    private void registerThreadPoolMetrics(MeterRegistry registry) {
        registerMetricsEventually("type", "ThreadPool", (name, allTags) -> {
            Gauge.builder("tomcat.threads.config.max", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "maxThreads")))
                    .tags(allTags)
                    .baseUnit(BaseUnits.THREADS)
                    .register(registry);

            Gauge.builder("tomcat.threads.busy", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "currentThreadsBusy")))
                    .tags(allTags)
                    .baseUnit(BaseUnits.THREADS)
                    .register(registry);

            Gauge.builder("tomcat.threads.current", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "currentThreadCount")))
                    .tags(allTags)
                    .baseUnit(BaseUnits.THREADS)
                    .register(registry);
        });
    }

    private void registerCacheMetrics(MeterRegistry registry) {
        registerMetricsEventually("type", "StringCache", (name, allTags) -> {
            FunctionCounter.builder("tomcat.cache.access", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "accessCount")))
                    .tags(allTags)
                    .register(registry);

            FunctionCounter.builder("tomcat.cache.hit", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "hitCount")))
                    .tags(allTags)
                    .register(registry);
        }, false);
    }

    private void registerServletMetrics(MeterRegistry registry) {
        registerMetricsEventually("j2eeType", "Servlet", (name, allTags) -> {
            FunctionCounter.builder("tomcat.servlet.error", mBeanServer,
                    s -> safeDouble(() -> s.getAttribute(name, "errorCount")))
                    .tags(allTags)
                    .register(registry);

            FunctionTimer.builder("tomcat.servlet.request", mBeanServer,
                    s -> safeLong(() -> s.getAttribute(name, "requestCount")),
                    s -> safeDouble(() -> s.getAttribute(name, "processingTime")), TimeUnit.MILLISECONDS)
                    .tags(allTags)
                    .register(registry);

            TimeGauge.builder("tomcat.servlet.request.max", mBeanServer, TimeUnit.MILLISECONDS,
                    s -> safeDouble(() -> s.getAttribute(name, "maxTime")))
                    .tags(allTags)
                    .register(registry);
        });
    }

    private void registerGlobalRequestMetrics(MeterRegistry registry) {
        registerMetricsEventually(KEY_TYPE, TYPE_GLOBAL_REQUEST_PROCESSOR, (name, allTags) -> {
            RequestGroupInfo requestGroupInfo;
            if (h2cRequestGroupInfo != null && isH2cObjectName(name)) {
                log.info("Use the caught h2cRequestGroupInfo");
                requestGroupInfo = h2cRequestGroupInfo;
            } else {
                requestGroupInfo = getRequestGroupInfo(mBeanServer, name);
            }

            if (requestGroupInfo == null) {
                return;
            }
            FunctionCounter.builder("tomcat.global.sent", mBeanServer,
                s -> safeDouble(() -> requestGroupInfo.getBytesSent()))
                .tags(allTags)
                .baseUnit(BaseUnits.BYTES)
                .register(registry);

            FunctionCounter.builder("tomcat.global.received", mBeanServer,
                s -> safeDouble(() -> requestGroupInfo.getBytesReceived()))
                .tags(allTags)
                .baseUnit(BaseUnits.BYTES)
                .register(registry);

            FunctionCounter.builder("tomcat.global.error", mBeanServer,
                    s -> safeDouble(() -> requestGroupInfo.getErrorCount()))
                    .tags(allTags)
                    .register(registry);

            FunctionTimer.builder("tomcat.global.request", mBeanServer,
                    s -> safeLong(() -> requestGroupInfo.getRequestCount()),
                    s -> safeDouble(() -> requestGroupInfo.getProcessingTime()), TimeUnit.MILLISECONDS)
                    .tags(allTags)
                    .register(registry);

            TimeGauge.builder("tomcat.global.request.max", mBeanServer, TimeUnit.MILLISECONDS,
                    s -> safeDouble(() -> requestGroupInfo.getMaxTime()))
                    .tags(allTags)
                    .register(registry);
        });
    }

    public static RequestGroupInfo getRequestGroupInfo(MBeanServer mBeanServer, ObjectName name) {
        if (mBeanServer instanceof JmxMBeanServer) {
            JmxMBeanServer jmxMBeanServer = (JmxMBeanServer)mBeanServer;
            try {
                Field mbsInterceptorField = JmxMBeanServer.class.getDeclaredField("mbsInterceptor");
                ReflectionUtils.makeAccessible(mbsInterceptorField);
                DefaultMBeanServerInterceptor mbsInterceptor = (DefaultMBeanServerInterceptor) mbsInterceptorField.get(jmxMBeanServer);
                Method getMBeanMethod = DefaultMBeanServerInterceptor.class.getDeclaredMethod("getMBean", ObjectName.class);
                ReflectionUtils.makeAccessible(getMBeanMethod);
                BaseModelMBean baseModelMBean = (BaseModelMBean) getMBeanMethod.invoke(mbsInterceptor, name);
                Field resourceField = BaseModelMBean.class.getDeclaredField("resource");
                ReflectionUtils.makeAccessible(resourceField);
                Object underlyingObj = resourceField.get(baseModelMBean);
                if (underlyingObj instanceof RequestGroupInfo) {
                    log.warn("new RequestGroupInfo created. objectName:{}, object: {}", name, underlyingObj);
                    return (RequestGroupInfo) underlyingObj;
                } else {
                    log.warn("This bean is not RequestGroupInfo, objectName: {}, object: {}", name, underlyingObj,
                            new RuntimeException("invoked too early."));
                }
            } catch (Exception e) {
                log.warn("Add tomcat global metrics failed.", e);
            }
        }
        return null;
    }

    public static boolean isH2cObjectName(ObjectName objectName) {
        if (objectName != null
                && MBEAN_SCHEMA.equals(objectName.getDomain())
                && MBEAN_TYPE.equals(objectName.getKeyProperty("type"))
                && PROTOCOL_H2C.equals(objectName.getKeyProperty("Upgrade"))) {
            return true;
        }

        return false;
    }

    private void registerMetricsEventually(String key, String value, BiConsumer<ObjectName, Iterable<Tag>> perObject) {
        registerMetricsEventually(key, value, perObject, true);
    }

    /**
     * If the MBean already exists, register metrics immediately. Otherwise register an MBean registration listener
     * with the MBeanServer and register metrics when/if the MBean becomes available.
     */
    private void registerMetricsEventually(String key, String value, BiConsumer<ObjectName, Iterable<Tag>> perObject, boolean hasName) {
        if (getJmxDomain() != null) {
            try {
                String name = getJmxDomain() + ":" + key + "=" + value + (hasName ? ",name=*,*" : "");
                Set<ObjectName> objectNames = this.mBeanServer.queryNames(new ObjectName(name), null);
                if (!objectNames.isEmpty()) {
                    // MBean is present, so we can register metrics now.
                    if (KEY_TYPE.equals(key) && TYPE_GLOBAL_REQUEST_PROCESSOR.equals(value)) {
                        objectNames.forEach(objectName -> {
                            String upgradeProtocol = objectName.getKeyProperty(OBJECT_NAME_PROPERTY_UPGRADE);
                            if (!StringUtils.hasText(upgradeProtocol)) {
                                upgradeProtocol = "http1";
                            }
                            Tags tags = nameTag(objectName);
                            tags = tags.and(TAG_NAME_PROTOCOL, upgradeProtocol);
                            perObject.accept(objectName, Tags.concat(this.tags, tags));
                        });
                    } else {
                        objectNames.stream().sorted(Comparator.reverseOrder()).findFirst()
                                .ifPresent(objectName -> perObject.accept(objectName, Tags.concat(tags, nameTag(objectName))));
                    }
                    return;
                }
            } catch (MalformedObjectNameException e) {
                // should never happen
                throw new RuntimeException("Error registering Tomcat JMX based metrics", e);
            }
        }

        // MBean isn't yet registered, so we'll set up a notification to wait for them to be present and register
        // metrics later.
        NotificationListener notificationListener = new NotificationListener() {
            @Override
            public void handleNotification(Notification notification, Object handback) {
                MBeanServerNotification mBeanServerNotification = (MBeanServerNotification) notification;
                ObjectName objectName = mBeanServerNotification.getMBeanName();
                perObject.accept(objectName, Tags.concat(tags, nameTag(objectName)));
                try {
                    mBeanServer.removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this);
                    notificationListeners.remove(this);
                } catch (InstanceNotFoundException | ListenerNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        notificationListeners.add(notificationListener);

        NotificationFilter notificationFilter = (NotificationFilter) notification -> {
            if (!MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(notification.getType())) {
                return false;
            }

            // we can safely downcast now
            ObjectName objectName = ((MBeanServerNotification) notification).getMBeanName();
            return objectName.getDomain().equals(getJmxDomain()) && objectName.getKeyProperty(key).equals(value);
        };

        try {
            mBeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, notificationListener, notificationFilter, null);
        } catch (InstanceNotFoundException e) {
            // should never happen
            throw new RuntimeException("Error registering MBean listener", e);
        }
    }

    private String getJmxDomain() {
        if (this.jmxDomain == null) {
            if (hasObjectName(OBJECT_NAME_SERVER_EMBEDDED)) {
                this.jmxDomain = JMX_DOMAIN_EMBEDDED;
            } else if (hasObjectName(OBJECT_NAME_SERVER_STANDALONE)) {
                this.jmxDomain = JMX_DOMAIN_STANDALONE;
            }
        }
        return this.jmxDomain;
    }

    /**
     * Set JMX domain. If unset, default values will be used as follows:
     *
     * <ul>
     *     <li>Embedded Tomcat: "Tomcat"</li>
     *     <li>Standalone Tomcat: "Catalina"</li>
     * </ul>
     *
     * @param jmxDomain JMX domain to be used
     * @since 1.0.11
     */
    public void setJmxDomain(String jmxDomain) {
        this.jmxDomain = jmxDomain;
    }

    private boolean hasObjectName(String name) {
        try {
            return this.mBeanServer.queryNames(new ObjectName(name), null).size() == 1;
        } catch (MalformedObjectNameException ex) {
            throw new RuntimeException(ex);
        }
    }

    private double safeDouble(Callable<Object> callable) {
        try {
            return Double.parseDouble(callable.call().toString());
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private long safeLong(Callable<Object> callable) {
        try {
            return Long.parseLong(callable.call().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    private Tags nameTag(ObjectName name) {
        String nameTagValue = name.getKeyProperty("name");
        if (nameTagValue != null) {
            return Tags.of("name", nameTagValue.replaceAll("\"", ""));
        }
        return Tags.empty();
    }

    @Override
    public void close() {
        for (NotificationListener notificationListener : this.notificationListeners) {
            try {
                this.mBeanServer.removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, notificationListener);
            } catch (InstanceNotFoundException | ListenerNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
