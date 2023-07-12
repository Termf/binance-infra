package com.binance.platform.monitor.configuration;

import java.io.File;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.monitor.metric.jvm.DiskSpaceMetrics;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
@Configuration
public class FileSystemAutoConfiguration {

    @Bean
    public DiskSpaceMetrics diskSpaceMetrics() {
        return new DiskSpaceMetrics(new File("."));
    }
}
