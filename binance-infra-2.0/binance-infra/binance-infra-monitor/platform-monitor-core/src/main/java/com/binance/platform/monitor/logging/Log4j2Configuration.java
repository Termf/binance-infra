package com.binance.platform.monitor.logging;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import com.binance.platform.monitor.logging.filter.Log4j2SampleFilter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.AbstractStringLayout.Serializer;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.monitor.logging.appender.log4j.AdvancedKafkaAppender;
import com.binance.platform.monitor.logging.appender.log4j.layout.CustomJsonLayout;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.net.NetUtil;

public class Log4j2Configuration extends LogConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2Configuration.class);

    private static final Map<String, AdvancedKafkaAppender> KAFKACACHE = Maps.newHashMap();

    private Map<String, String> mixedFields = Maps.newHashMap();

    public Log4j2Configuration(Environment env) {
        super(env);
        String localIp = NetUtil.getLocalHost();
        String logAppName = getProperty("spring.application.logappname");
        if (logAppName == null) {
            logAppName = getProperty("spring.application.name");
        }
        mixedFields.put(CustomJsonLayout.MIXEDFIELDS_HOST_NAME, localIp);
        mixedFields.put(CustomJsonLayout.MIXEDFIELDS_APP_NAME, logAppName);
    }

    /**
     * 添加日志采样Filter
     */
    public static void addSampleFilter() {
        LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        Filter preFilter = configuration.getFilter();
        if (preFilter == null) {
            configuration.addFilter(new Log4j2SampleFilter());
        } else {
            configuration.removeFilter(preFilter);
            CompositeFilter newFilter = createSampleFilter(preFilter);
            configuration.addFilter(newFilter);
        }
    }

    /**
     * 采样的Filter放在前面执行
     *
     * @return
     */
    private static CompositeFilter createSampleFilter(Filter preFilter) {
        CompositeFilter compositeFilter = CompositeFilter.createFilters(new Filter[]{new Log4j2SampleFilter()});
        return compositeFilter.addFilter(preFilter);
    }

    private void createKafkaAppender() {
        if (super.getBootstrapservers() != null) {
            String appenderName = "AdvancedKafkaAppender";
            LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
            Configuration configuration = loggerContext.getConfiguration();
            Map<String, Appender> appenders = configuration.getAppenders();
            // 删除默认的kafka,添加自定义的kafka
            appenders.remove("Kafka");
            if (!appenders.containsKey(appenderName)) {
                AdvancedKafkaAppender kafkaAppender = KAFKACACHE.get(appenderName);
                if (kafkaAppender == null) {
                    kafkaAppender = AdvancedKafkaAppender.createAppender(
                        CustomJsonLayout.createDefaultLayout(configuration, mixedFields), null, configuration,
                        appenderName, getKafkaTopic(), getBootstrapservers());
                    KAFKACACHE.put(appenderName, kafkaAppender);
                }
                kafkaAppender.start();
                org.apache.logging.log4j.core.Logger rootLogger = loggerContext.getRootLogger();
                rootLogger.addAppender(kafkaAppender);
            }
        }
    }

    private void createConsoleAppender() {
        LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
        Map<String, Appender> appenders = loggerContext.getConfiguration().getAppenders();
        boolean haveConsoleAppender = false;
        Iterator<Appender> it = appenders.values().iterator();
        while (it.hasNext()) {
            Appender appender = it.next();
            if (appender instanceof ConsoleAppender) {
                haveConsoleAppender = true;
            }
        }
        // 如果没有consoleAppender,添加一个
        if (!haveConsoleAppender) {
            PatternLayout layout = PatternLayout.newBuilder()//
                .withPattern(super.getLog4jPattern())//
                .withCharset(Charset.forName("UTF-8"))//
                .withAlwaysWriteExceptions(true)//
                .build();
            ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(layout);
            consoleAppender.start();
            org.apache.logging.log4j.core.Logger rootLogger = loggerContext.getRootLogger();
            consoleAppender.addFilter(ThresholdFilter.createFilter(rootLogger.getLevel(), Result.ACCEPT, Result.DENY));
            rootLogger.addAppender(consoleAppender);

        }
    }

    public static void resetConsoleAppenderLevel() {
        LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
        Map<String, Appender> appenders = loggerContext.getConfiguration().getAppenders();
        appenders.forEach((k, v) -> {
            if (v instanceof ConsoleAppender) {
                ((ConsoleAppender)v).addFilter(ThresholdFilter.createFilter(Level.OFF, Result.ACCEPT, Result.DENY));
            }
        });
    }

    @Override
    public void init() {
        try {
            LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
            if (loggerContext == null) {
                return;
            }
            createKafkaAppender();
            createConsoleAppender();
            Configuration configuration = loggerContext.getConfiguration();
            if (isEnableSkywalking()) {
                configuration.getPluginPackages().add("org.apache.skywalking.apm.toolkit.log.log4j.v2.x");
            }
            Map<String, Appender> appenders = configuration.getAppenders();
            String pattern = super.getLog4jPattern();
            for (Appender appender : appenders.values()) {
                Layout<? extends Serializable> layout = appender.getLayout();
                if (layout instanceof PatternLayout) {
                    Field oldSerializer = ReflectionUtils.findField(layout.getClass(), "eventSerializer");
                    if (oldSerializer == null) {
                        continue;
                    } else {
                        ReflectionUtils.makeAccessible(oldSerializer);
                        Serializer newserializer = PatternLayout.createSerializer(configuration,
                            findRegexReplacementByReflection(layout, oldSerializer), pattern, pattern, null, true,
                            true);
                        replaceEventSerializer(layout, newserializer, oldSerializer);
                    }
                }
            }
            loggerContext.updateLoggers();
        } catch (Throwable e) {
            logger.warn(e.getMessage());
        }
    }

    private RegexReplacement findRegexReplacementByReflection(Object patternLayOut, Field oldserializer) {
        Serializer serializer = (Serializer)ReflectionUtils.getField(oldserializer, patternLayOut);
        Field fieldRegexReplacement = ReflectionUtils.findField(serializer.getClass(), "replace");
        ReflectionUtils.makeAccessible(fieldRegexReplacement);
        return (RegexReplacement)ReflectionUtils.getField(fieldRegexReplacement, serializer);
    }

    private void replaceEventSerializer(Object patternLayOut, Serializer newserializer, Field oldserializer)
        throws Throwable {
        Field modifersField = Field.class.getDeclaredField("modifiers");
        modifersField.setAccessible(true);
        modifersField.setInt(oldserializer, oldserializer.getModifiers() & ~Modifier.FINAL);
        ReflectionUtils.setField(oldserializer, patternLayOut, newserializer);
    }

}
