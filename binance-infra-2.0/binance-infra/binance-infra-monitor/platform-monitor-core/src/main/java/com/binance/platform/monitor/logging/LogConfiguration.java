package com.binance.platform.monitor.logging;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public abstract class LogConfiguration {

	private static final String LOG_KAFKA_TOPIC = "managent.logs.kafka.topic";
	private static final String LOG_KAFKA_BOOTSTRAPSERVERS = "managent.logs.kafka.bootstrapservers";

	private final Environment env;

	private static final String LOG_PATTERN_KEY = "managent.logs.pattern";

	private static final String LOGBACK_PATTERN_DEFAULT = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%thread] [%-5level][${sys:local-ip}][%logger{40}] [%tid] - %msg%n";

	private static final String LOG4J2_PATTERN_DEFAULT = "[%-p] [%d{yyyy.MM.dd HH:mm:ss.SSS}] [${sys:local-ip}] [%X{traceId}] [%t] [%c(%L)] - %m%n";

	public String getBootstrapservers() {
		return env.getProperty(LOG_KAFKA_BOOTSTRAPSERVERS);
	}

	public String getKafkaTopic() {
		return env.getProperty(LOG_KAFKA_TOPIC, "bizLog");
	}

	public String getLogBackPattern() {
		return env.getProperty(LOG_PATTERN_KEY, LOGBACK_PATTERN_DEFAULT);
	}

	public String getLog4jPattern() {
		return env.getProperty(LOG_PATTERN_KEY, LOG4J2_PATTERN_DEFAULT);
	}

	public LogConfiguration(Environment env) {
		this.env = env;
	}

	public String getProperty(String key) {
		return this.env.getProperty(key);
	}

	public boolean isEnableSkywalking() {
		if (islog4j2()) {
			return StringUtils.contains(getLog4jPattern(), "[%traceId]");
		}
		if (isLogback()) {
			return StringUtils.contains(getLogBackPattern(), "[%tid]");
		}
		return false;
	}

	public abstract void init();

	/**
	 * help method
	 */

	public static LogConfiguration createLogConfiguration(ConfigurableEnvironment environment) {
		if (isLogback()) {
			return new LogBackConfiguration(environment);
		} else if (islog4j2()) {
			return new Log4j2Configuration(environment);
		}
		return null;
	}

	public static boolean isLogback() {
		return isPresent("ch.qos.logback.classic.Logger") && isPresent("ch.qos.logback.core.Appender");
	}

	public static boolean islog4j2() {
		return isPresent("org.apache.logging.log4j.Logger") && isPresent("org.apache.logging.log4j.core.Layout");
	}

	public static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

}
