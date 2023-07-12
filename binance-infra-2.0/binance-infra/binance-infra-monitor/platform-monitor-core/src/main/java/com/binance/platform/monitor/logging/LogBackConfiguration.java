package com.binance.platform.monitor.logging;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.monitor.logging.appender.logback.AdvancedKafkaAppender;
import com.binance.platform.monitor.logging.appender.logback.layout.CustomJsonLayout;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.net.NetUtil;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.AppenderAttachable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LogBackConfiguration extends LogConfiguration {
    private static final Map<String, AdvancedKafkaAppender> KAFKACACHE = Maps.newHashMap();

    private Map<String, String> mixedFields = Maps.newHashMap();

    public LogBackConfiguration(Environment env) {
        super(env);
        String localIp = NetUtil.getLocalHost();
        String logAppName = getProperty("spring.application.logappname");
        if (logAppName == null) {
            logAppName = getProperty("spring.application.name");
        }
        mixedFields.put(CustomJsonLayout.MIXEDFIELDS_HOST_NAME, localIp);
        mixedFields.put(CustomJsonLayout.MIXEDFIELDS_APP_NAME, logAppName);
    }

    private void createBizLogger() {
        if (super.getBootstrapservers() != null) {
            LoggerContext content = (LoggerContext)LoggerFactory.getILoggerFactory();
            Logger rootLogger = content.getLogger(Logger.ROOT_LOGGER_NAME);
            String appenderName = "AdvancedKafkaAppender";
            // 删除默认的kafka,添加自定义的kafka
            rootLogger.detachAppender("Kafka");
            if (rootLogger.getAppender(appenderName) == null) {
                CustomJsonLayout layout = CustomJsonLayout.createLayout(mixedFields);
                AdvancedKafkaAppender kafkaAppender = KAFKACACHE.get(appenderName);
                if (kafkaAppender == null) {
                    kafkaAppender = new AdvancedKafkaAppender();
                    kafkaAppender.setName(appenderName);
                    kafkaAppender.setLayout(layout);
                    kafkaAppender.setTopic(getKafkaTopic());
                    kafkaAppender.setBootstrapServers(getBootstrapservers());
                    kafkaAppender.setContext(content);
                    KAFKACACHE.put(appenderName, kafkaAppender);
                }
                kafkaAppender.start();
                ThresholdFilter filter = new ThresholdFilter();
                filter.setLevel(rootLogger.getLevel().levelStr);
                kafkaAppender.addFilter(filter);
                rootLogger.addAppender(kafkaAppender);
            }
        }
    }

    @Override
    public void init() {
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        if (loggerContext == null) {
            return;
        }
        List<Logger> loggerList = loggerContext.getLoggerList();
        createBizLogger();
        for (Logger logger : loggerList) {
            AppenderAttachable appenderAttachable = logger;
            setLayout(loggerContext, appenderAttachable.iteratorForAppenders());
        }
    }

    public static void resetConsoleAppenderLevel() {
        LoggerContext content = (LoggerContext)LoggerFactory.getILoggerFactory();
        Logger rootLogger = content.getLogger(Logger.ROOT_LOGGER_NAME);
        Iterator<Appender<ILoggingEvent>> it = rootLogger.iteratorForAppenders();
        while (it.hasNext()) {
            Appender<ILoggingEvent> appender = it.next();
            if (appender instanceof ConsoleAppender) {
                ThresholdFilter filter = new ThresholdFilter();
                filter.setLevel("OFF");
                ((ConsoleAppender)appender).addFilter(filter);
            }
        }
    }

    private void setLayout(LoggerContext loggerContext, Iterator<Appender<?>> iterator) {
        while (iterator.hasNext()) {
            Appender appender = iterator.next();
            if (appender instanceof OutputStreamAppender) {
                setLayout(loggerContext, (OutputStreamAppender<?>)appender);
            } else if (appender instanceof AsyncAppenderBase) {
                AsyncAppenderBase asyncAppenderBase = (AsyncAppenderBase)appender;
                setLayout(loggerContext, asyncAppenderBase.iteratorForAppenders());
            }
        }
    }

    private void setLayout(LoggerContext loggerContext, OutputStreamAppender outputStreamAppender) {
        Encoder<?> encoder = outputStreamAppender.getEncoder();
        if (encoder instanceof PatternLayoutEncoder) {
            if (isEnableSkywalking()) {
                TraceIdPatternLogbackLayout traceIdLayOut = new TraceIdPatternLogbackLayout();
                traceIdLayOut.setContext(loggerContext);
                traceIdLayOut.setPattern(getLogBackPattern());
                traceIdLayOut.start();
                Field field = ReflectionUtils.findField(encoder.getClass(), "layout");
                field.setAccessible(true);
                ReflectionUtils.setField(field, encoder, traceIdLayOut);
            } else {
                PatternLayoutEncoder layout = (PatternLayoutEncoder)encoder;
                layout.setContext(loggerContext);
                layout.setPattern(getLogBackPattern());
                layout.start();
            }

        }
    }

}
