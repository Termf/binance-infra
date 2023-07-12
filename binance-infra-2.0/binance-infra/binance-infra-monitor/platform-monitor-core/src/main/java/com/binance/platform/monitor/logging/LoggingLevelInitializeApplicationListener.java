package com.binance.platform.monitor.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;

import java.util.Set;

/**
 * refresh logging level with apollo configs
 *
 * @author Thomas Li
 * Date: 2021/1/26 2:34 下午
 */
public class LoggingLevelInitializeApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent> {
    private static final Logger log = LoggerFactory.getLogger(LoggingLevelInitializeApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        SpringApplication springApplication = event.getSpringApplication();
        String[] args = event.getArgs();
        Set<ApplicationListener<?>> listeners = springApplication.getListeners();
        for (ApplicationListener<?> listener : listeners) {
            if (listener instanceof LoggingApplicationListener) {
                log.info("refresh logging level with apollo configs");
                ((LoggingApplicationListener) listener).onApplicationEvent(
                        new ApplicationEnvironmentPreparedEvent(springApplication, args, event.getApplicationContext().getEnvironment())
                );
            }
        }
    }

}
