package com.binance.platform.monitor.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.jvm.MyJvmGcMetrics;
import com.binance.platform.monitor.metric.jvm.ProcessMemoryMetrics;
import com.binance.platform.monitor.metric.jvm.ProcessThreadMetrics;
import com.binance.platform.monitor.metric.system.MemoryMetrics;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
@Configuration
public class JVMAutoConfiguration {

    @Autowired
    private CustomizerTag platformTag;

    @Bean
    @ConditionalOnMissingBean
    public ClassLoaderMetrics classLoaderMetrics() {
        return new ClassLoaderMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmGcMetrics jvmGcMetrics() {
        return new JvmGcMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmMemoryMetrics jvmMemoryMetrics() {
        return new JvmMemoryMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public JvmThreadMetrics jvmThreadMetrics() {
        return new JvmThreadMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public ProcessMemoryMetrics processMemoryMetrics() {
        return new ProcessMemoryMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public ProcessThreadMetrics processThreadMetrics() {
        return new ProcessThreadMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public MyJvmGcMetrics myJvmGcMetrics() {
        return new MyJvmGcMetrics(platformTag.getTags());
    }

}
