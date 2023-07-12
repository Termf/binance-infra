package com.binance.platform.monitor.configuration;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.system.MemoryMetrics;
import com.binance.platform.monitor.metric.system.TcpUdpMetrics;
import com.binance.platform.monitor.metric.system.NetworkMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
@Configuration
public class SystemAutoConfiguration {

    @Autowired
    private CustomizerTag platformTag;

    @Autowired
    private Environment env;

    @Bean
    @ConditionalOnMissingBean
    public NetworkMetrics networkMetrics() {
        return new NetworkMetrics(platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public TcpUdpMetrics tcpUdpMetric() {
        return new TcpUdpMetrics(env, platformTag.getTags());
    }

    @Bean
    @ConditionalOnMissingBean
    public MemoryMetrics memoryMetrics() {
        return new MemoryMetrics(platformTag.getTags());
    }
}
