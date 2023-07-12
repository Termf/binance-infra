package com.binance.platform.monitor.configuration.cloudwatch;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "management.metrics.export.cloudwatch")
public class CloudWatchProperties extends StepRegistryProperties {

	/**
	 * The namespace which will be used when sending metrics to CloudWatch. This
	 * property is needed and must not be null.
	 */
	private String namespace = "";

	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}