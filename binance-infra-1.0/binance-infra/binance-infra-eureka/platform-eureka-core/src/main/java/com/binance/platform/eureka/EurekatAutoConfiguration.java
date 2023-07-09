package com.binance.platform.eureka;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.binance.platform.eureka.event.EurekaClientEventListener;
import com.netflix.discovery.EurekaClient;

@Configuration
public class EurekatAutoConfiguration {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired(required = false)
    private LogLevelSetter logLevelSetter;

    private static final String[] LOGGERS = new String[] {"com.netflix.discovery.DiscoveryClient"};

    @PostConstruct
    public void init() {
        eurekaClient.registerEventListener(new EurekaClientEventListener(applicationEventPublisher));
        if (logLevelSetter != null) {
            for (String logger : LOGGERS) {
                logLevelSetter.setLoggerLevel(logger, "ERROR");
            }
        }

    }

    @Configuration
    static class LogLevelSetterConfig {
        @Bean
        @ConditionalOnClass(name = "org.apache.logging.log4j.core.Logger")
        public LogLevelSetter log4j2LevelSetter() {
            return new LogLevelSetter() {
                @Override
                public void setLoggerLevel(String name, String level) {
                    Collection<org.apache.logging.log4j.core.Logger> notCurrentLoggerCollection =
                        org.apache.logging.log4j.core.LoggerContext.getContext(false).getLoggers();
                    Collection<org.apache.logging.log4j.core.Logger> currentLoggerCollection =
                        org.apache.logging.log4j.core.LoggerContext.getContext().getLoggers();
                    Collection<org.apache.logging.log4j.core.Logger> loggerCollection = notCurrentLoggerCollection;
                    loggerCollection.addAll(currentLoggerCollection);
                    for (org.apache.logging.log4j.core.Logger logger : loggerCollection) {
                        if (name.equals(logger.getName())) {
                            logger.setLevel(org.apache.logging.log4j.Level.toLevel(level));
                        }
                    }
                }
            };
        }

        @Bean
        @ConditionalOnClass(name = "ch.qos.logback.classic.Logger")
        public LogLevelSetter logbackLevelSetter() {
            return new LogLevelSetter() {
                @Override
                public void setLoggerLevel(String name, String level) {
                    ch.qos.logback.classic.LoggerContext loggerContext =
                        (ch.qos.logback.classic.LoggerContext)LoggerFactory.getILoggerFactory();
                    List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
                    for (ch.qos.logback.classic.Logger logger : loggerList) {
                        if (name.equals(logger.getName())) {
                            logger.setLevel(ch.qos.logback.classic.Level.valueOf(level));
                        }
                    }
                }
            };
        }
    }

    public static interface LogLevelSetter {

        void setLoggerLevel(String name, String level);
    }

}