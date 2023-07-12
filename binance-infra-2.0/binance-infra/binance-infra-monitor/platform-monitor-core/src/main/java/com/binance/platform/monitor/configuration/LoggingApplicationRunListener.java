package com.binance.platform.monitor.configuration;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.logging.Log4j2Configuration;
import com.binance.platform.monitor.logging.LogBackConfiguration;
import com.binance.platform.monitor.logging.LogConfiguration;
import com.binance.platform.monitor.logging.SampleUtil;
import com.binance.platform.monitor.logging.exception.GlobalUncaughtExceptionHandler;

public class LoggingApplicationRunListener implements SpringApplicationRunListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingApplicationRunListener.class);

    private static final String MANAGEMENT_LOGGING_ENABLE = "management.logging.enable";

    public LoggingApplicationRunListener(SpringApplication application, String[] args) {}

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        try {
            System.setProperty("localhost.default.nic.list", "bond0,eth0,em0,br0,en0,gpd0");
            if (BooleanUtils.toBoolean(environment.getProperty(MANAGEMENT_LOGGING_ENABLE, "true"))) {
                LogConfiguration logConfiguration = LogConfiguration.createLogConfiguration(environment);
                logConfiguration.init();
            }
            SampleUtil.init(environment);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
        Thread.setDefaultUncaughtExceptionHandler(new GlobalUncaughtExceptionHandler());
    }

    @Override
    public void starting() {}

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {}

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {}

    @Override
    public void started(ConfigurableApplicationContext context) {
        if (BooleanUtils.toBoolean(context.getEnvironment().getProperty(MANAGEMENT_LOGGING_ENABLE, "true"))) {
            if (LogConfiguration.islog4j2() && (EnvUtil.isEcs() || EnvUtil.isK8s() || EnvUtil.isNotMacOs())) {
                logger.info("will resetConsoleAppenderLevel");
                Log4j2Configuration.resetConsoleAppenderLevel();
            }
            if (LogConfiguration.isLogback() && (EnvUtil.isEcs() || EnvUtil.isK8s() || EnvUtil.isNotMacOs())) {
                logger.info("will resetConsoleAppenderLevel");
                LogBackConfiguration.resetConsoleAppenderLevel();
            }
        }
        if (LogConfiguration.islog4j2()) {
            Log4j2Configuration.addSampleFilter();
        }
    }

    @Override
    public void running(ConfigurableApplicationContext context) {}

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {}

}
