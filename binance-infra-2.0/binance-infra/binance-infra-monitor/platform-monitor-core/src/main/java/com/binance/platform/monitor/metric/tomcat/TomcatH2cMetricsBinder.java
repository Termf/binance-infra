/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.binance.platform.monitor.metric.tomcat;

import java.util.Collections;
import java.util.Set;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.tomcat.TomcatMetrics;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Manager;

import org.apache.coyote.RequestGroupInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.web.tomcat.TomcatMetricsBinder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;

import javax.annotation.PreDestroy;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

/**
 * Binds {@link TomcatMetrics} in response to the {@link ApplicationStartedEvent}.
 *
 * @author Andy Wilkinson
 * @since 2.1.0
 */
public class TomcatH2cMetricsBinder extends TomcatMetricsBinder {
	private static final Logger log = LoggerFactory.getLogger(TomcatH2cMetricsBinder.class);

	private final MeterRegistry meterRegistry;

	private final Iterable<Tag> tags;
	private RequestGroupInfo h2cRequestGroupInfo;
	private TomcatH2cMetrics tomcatH2cMetrics;
	private NotificationListener notificationListener;

	public TomcatH2cMetricsBinder(MeterRegistry meterRegistry) {
		this(meterRegistry, Collections.emptyList());
	}

	public TomcatH2cMetricsBinder(MeterRegistry meterRegistry, Iterable<Tag> tags) {
		super(meterRegistry);
		this.meterRegistry = meterRegistry;
		this.tags = tags;
		cacheH2cRequestGroupInfo();
	}

	private void cacheH2cRequestGroupInfo() {
		MBeanServer mBeanServer = TomcatH2cMetrics.getMBeanServer();
		try {
			Set<ObjectName> h2cObjectNameSet = mBeanServer.queryNames(new ObjectName(TomcatH2cMetrics.MBEAN_SCHEMA
					+ ":type=" + TomcatH2cMetrics.MBEAN_TYPE
					+ ",Upgrade=" + TomcatH2cMetrics.PROTOCOL_H2C
					+ ",*"), null);
			if (!h2cObjectNameSet.isEmpty()) {
				h2cRequestGroupInfo = TomcatH2cMetrics.getRequestGroupInfo(mBeanServer, h2cObjectNameSet.iterator().next());
				log.info("cache the existed h2c RequestGroupInfo");
			}
			notificationListener = new NotificationListener() {
				@Override
				public void handleNotification(Notification notification, Object handback) {
					if (notification instanceof MBeanServerNotification) {
						ObjectName objectName = ((MBeanServerNotification) notification).getMBeanName();
						if (TomcatH2cMetrics.isH2cObjectName(objectName)) {
							RequestGroupInfo requestGroupInfo = TomcatH2cMetrics.getRequestGroupInfo(mBeanServer, objectName);
							if (requestGroupInfo != null) {
								h2cRequestGroupInfo = requestGroupInfo;
								log.info("caught h2cRequestGroupInfo instance. mbean.type:{}", notification.getType());
							}

							removeNotification();
						}
					}
				}
			};
			NotificationFilter notificationFilter = (NotificationFilter) notification -> {
				if (!MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(notification.getType())) {
					return false;
				}

				return true;
			};
			try {
				mBeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, notificationListener, notificationFilter, null);
			} catch (InstanceNotFoundException e) {
				log.error(e.getMessage(), e);
			}
		} catch (MalformedObjectNameException e) {
			log.warn("search h2c mbean error", e);
		}
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		Manager manager = findManager(applicationContext);
		tomcatH2cMetrics = new TomcatH2cMetrics(manager, this.tags, h2cRequestGroupInfo);
		tomcatH2cMetrics.bindTo(this.meterRegistry);
		removeNotification();
	}

	@PreDestroy
	public void destroy() {
		tomcatH2cMetrics.close();
		removeNotification();
	}

	private void removeNotification() {
		if (notificationListener != null) {
			try {
				TomcatH2cMetrics.getMBeanServer().removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, notificationListener);
			} catch (Exception e) {
				log.warn("remove TomcatH2cMetrics mbean listener error", e);
			}
			notificationListener = null;
		}
	}

	private Manager findManager(ApplicationContext applicationContext) {
		if (applicationContext instanceof WebServerApplicationContext) {
			WebServer webServer = ((WebServerApplicationContext) applicationContext).getWebServer();
			if (webServer instanceof TomcatWebServer) {
				Context context = findContext((TomcatWebServer) webServer);
				return context.getManager();
			}
		}
		return null;
	}

	private Context findContext(TomcatWebServer tomcatWebServer) {
		for (Container container : tomcatWebServer.getTomcat().getHost().findChildren()) {
			if (container instanceof Context) {
				return (Context) container;
			}
		}
		return null;
	}

}
