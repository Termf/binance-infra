package com.binance.platform.monitor.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.threadpool.ThreadPoolBinder;

@AutoConfigureAfter(MicrometerAutoConfiguration.class)
@Configuration
public class ThreadPoolAutoConfiguration {

    @Autowired(required = false)
    private Map<String, ThreadPoolTaskExecutor> executors;

    @Autowired(required = false)
    private Map<String, ThreadPoolTaskScheduler> schedulerExecutors;

    @Autowired
    CustomizerTag platformTag;

    @Bean
    public ThreadPoolBinder threadPoolBinder() {
        return new ThreadPoolBinder(executors, schedulerExecutors, platformTag);
    }
}
