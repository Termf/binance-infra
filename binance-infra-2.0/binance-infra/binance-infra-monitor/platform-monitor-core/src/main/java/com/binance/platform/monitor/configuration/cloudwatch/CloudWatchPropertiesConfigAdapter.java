package com.binance.platform.monitor.configuration.cloudwatch;

import java.time.Duration;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryPropertiesConfigAdapter;

import com.binance.platform.monitor.configuration.cloudwatch.internal.CloudWatchConfig;

class CloudWatchPropertiesConfigAdapter extends StepRegistryPropertiesConfigAdapter<CloudWatchProperties>
    implements CloudWatchConfig {

    CloudWatchPropertiesConfigAdapter(CloudWatchProperties properties) {
        super(properties);
    }

    @Override
    public String namespace() {
        return get(CloudWatchProperties::getNamespace, CloudWatchConfig.super::namespace);
    }

    @Override
    public int batchSize() {
        return get(CloudWatchProperties::getBatchSize, CloudWatchConfig.super::batchSize);
    }

    @Override
    public Duration step() {
        return get(CloudWatchProperties::getStep, CloudWatchConfig.super::step);
    }
}
