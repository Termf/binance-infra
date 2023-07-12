package com.binance.platform.monitor.configuration.cloudwatch.internal;

import java.time.Duration;

import io.micrometer.core.instrument.config.InvalidConfigurationException;
import io.micrometer.core.instrument.config.MissingRequiredConfigurationException;
import io.micrometer.core.instrument.step.StepRegistryConfig;

public interface CloudWatchConfig extends StepRegistryConfig {

    int MAX_BATCH_SIZE = 20;

    @Override
    default String prefix() {
        return "cloudwatch";
    }

    default String namespace() {
        String v = get(prefix() + ".namespace");
        if (v == null)
            throw new MissingRequiredConfigurationException("namespace must be set to report metrics to CloudWatch");
        return v;
    }

    @Override
    default Duration step() {
        String v = get(prefix() + ".step");
        return v == null ? Duration.ofMinutes(1) : Duration.parse(v);
    }

    @Override
    default int batchSize() {
        String v = get(prefix() + ".batchSize");
        if (v == null) {
            return MAX_BATCH_SIZE;
        }
        int vInt = Integer.parseInt(v);
        if (vInt > MAX_BATCH_SIZE)
            throw new InvalidConfigurationException("batchSize must be <= " + MAX_BATCH_SIZE);

        return vInt;
    }

}
