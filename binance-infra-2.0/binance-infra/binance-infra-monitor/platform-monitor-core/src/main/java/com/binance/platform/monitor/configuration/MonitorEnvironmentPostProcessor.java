package com.binance.platform.monitor.configuration;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.binance.platform.common.ExcludeAutoConfigurationUtil;

public class MonitorEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	private static final String DEFAULT_PROPERTY = "META-INF/monitor-client/bootstrap.properties";

	private static final String APPENDER = "p6spy.config.appender";
	private static final String CUSTOM_LOG_MESSAGE_FORMAT = "p6spy.config.customLogMessageFormat";
	private static final String LOG_MESSAGE_FORMAT = "p6spy.config.logMessageFormat";
	private static final String EXECUTION_THRESHOLD = "p6spy.config.executionThreshold";
	private static final String OUTAGE_DETECTION = "p6spy.config.outagedetection";
	private static final String OUTAGE_DETECTION_INTERVAL = "p6spy.config.outagedetectioninterval";

	private static final String[] EXCLUDEAUTOCONFIGALL = new String[] {
			"io.micrometer.spring.autoconfigure.Log4J2MetricsAutoConfiguration",
			"io.micrometer.spring.autoconfigure.LogbackMetricsAutoConfiguration",

			// Tomcat默认的metrics只监控了Http1.1, 我们还需要监控h2c
			"org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration",

			// 以下这些服务是aws的pass服务，不需要的
			"org.springframework.cloud.aws.autoconfigure.context.ContextResourceLoaderAutoConfiguration", // aws的s3
			"org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration", // aws的CloudFormation
			"org.springframework.cloud.aws.autoconfigure.mail.MailSenderAutoConfiguration", // aws的mail
			"org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration", // aws的es
			"org.springframework.cloud.aws.autoconfigure.messaging.MessagingAutoConfiguration", // aws的message
			"org.springframework.cloud.aws.autoconfigure.jdbc.AmazonRdsDatabaseAutoConfiguration", // aws的rds

			// 禁用springboot actuator自动装载kafka consumer监控自动配置，使用自己实现的
			"org.springframework.boot.actuate.autoconfigure.metrics.KafkaMetricsAutoConfiguration",

			// 这个是需要自己的定义的，覆盖掉了
			"org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchExportAutoConfiguration" };

	private static Properties DEFAULTPROPERTIES;;

	static {
		try {
			ClassPathResource resource = new ClassPathResource(DEFAULT_PROPERTY);
			DEFAULTPROPERTIES = PropertiesLoaderUtils.loadProperties(resource);
		} catch (Exception e) {
		}
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		System.setProperty(APPENDER, "com.binance.platform.monitor.metric.db.p6spy.SQLLogger");
		System.setProperty(CUSTOM_LOG_MESSAGE_FORMAT,
				"%(executionTime)ms|%(category)|connection%(connectionId)|%(effectiveSqlSingleLine)");
		System.setProperty(LOG_MESSAGE_FORMAT, "com.p6spy.engine.spy.appender.CustomLineFormat");
		System.setProperty(OUTAGE_DETECTION, "true");
		boolean showSqlWithRealParameter = Boolean
				.valueOf(environment.getProperty("monitor.sql.with-real-parameter", "true"));
		int executionThreshold = Integer.valueOf(environment.getProperty("monitor.sql.executionthreshold", "0"));
		int outagedetectioninterval = Integer
				.valueOf(environment.getProperty("monitor.sql.outagedetectioninterval", "5"));
		if (showSqlWithRealParameter) {
			System.setProperty(CUSTOM_LOG_MESSAGE_FORMAT,
					"%(executionTime)ms|%(category)|connection%(connectionId)|%(sqlSingleLine)");
		}
		if (executionThreshold > 0) {
			System.setProperty(EXECUTION_THRESHOLD, String.valueOf(executionThreshold));
		} else {
			System.setProperty(EXECUTION_THRESHOLD, String.valueOf(0));
		}
		if (outagedetectioninterval > 0) {
			System.setProperty(OUTAGE_DETECTION_INTERVAL, String.valueOf(outagedetectioninterval));
		} else {
			System.setProperty(EXECUTION_THRESHOLD, String.valueOf(5));
		}
		if (this.isServlet()) {
			environment.getPropertySources()
					.addLast(new PropertiesPropertySource("ManagementPropertySources", DEFAULTPROPERTIES));
		}
		ExcludeAutoConfigurationUtil.excludeAutoConfiguration(environment, EXCLUDEAUTOCONFIGALL);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}

	private boolean isServlet() {
		try {
			Class.forName("org.springframework.web.context.support.GenericWebApplicationContext");
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

}
