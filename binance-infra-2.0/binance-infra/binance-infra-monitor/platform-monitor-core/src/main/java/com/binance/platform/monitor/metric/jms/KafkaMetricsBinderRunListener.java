package com.binance.platform.monitor.metric.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/9/7
 */
public class KafkaMetricsBinderRunListener implements SpringApplicationRunListener {
    private SpringApplication application;
    private String[] args;

    public KafkaMetricsBinderRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        context.publishEvent(new KafkaMetricsBinder.DoKafkaMetricsBindEvent(application, args, context));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
