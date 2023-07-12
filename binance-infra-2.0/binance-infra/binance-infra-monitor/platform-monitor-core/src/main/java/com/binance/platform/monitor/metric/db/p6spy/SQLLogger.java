package com.binance.platform.monitor.metric.db.p6spy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.binance.platform.common.AlarmUtil;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.p6spy.engine.common.Loggable;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;

public class SQLLogger extends FormattedLogger {
    private Logger log;
    private boolean monitorsql = false;
    private int alarmsql = 0;
    private String appName = StringUtils.EMPTY;
    private boolean isProd = true;

    public static ApplicationEventPublisher applicationEventPublisher;

    private static String SLOW_SQL_TEMPLATE = "慢SQL告警:【 %s 】SQL耗时:【 %sS 】UUID:【%s】 \n %s";

    private static String EXCEPTION_SQL_TEMPLATE = "SQL异常告警: 【 %s 】\n %s";

    public SQLLogger() {
        this.log = LoggerFactory.getLogger("monitor.sql");
        this.isProd = EnvUtil.isProd() && !EnvUtil.isUSA();
        this.appName = EnvUtil.getProperty("spring.application.name");
    }

    private void doGetProperty() {
        boolean monitorsql = BooleanUtils.toBoolean(EnvUtil.getProperty("monitor.sql.log", "true"));
        String alarmsql = EnvUtil.getProperty("monitor.sql.alarm", "5");
        this.monitorsql = monitorsql;
        this.alarmsql = Integer.valueOf(alarmsql);
    }

    private void doLog(String msg, Category category) {
        try {
            if (monitorsql) {
                if (Category.ERROR.equals(category)) {
                    log.error(msg);
                } else if (Category.WARN.equals(category)) {
                    log.warn(msg);
                } else if (Category.DEBUG.equals(category)) {
                    log.debug(msg);
                } else {
                    log.info(msg);
                }
            }
        } finally {
            doGetProperty();
        }
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        if (Category.ERROR.equals(category)) {
            return log.isErrorEnabled();
        } else if (Category.WARN.equals(category)) {
            return log.isWarnEnabled();
        } else if (Category.DEBUG.equals(category)) {
            return log.isDebugEnabled();
        } else {
            return log.isInfoEnabled();
        }
    }

    @Override
    public void logException(Exception e) {
        if (isProd && EnvUtil.isAlarmService(this.appName) && alarmsql > 0) {
            StringBuilder message = new StringBuilder();
            if (e != null) {
                message.append(" ");
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                StringBuffer error = stringWriter.getBuffer();
                message.append(error.toString());
            }
            AlarmUtil.alarmTeams(String.format(EXCEPTION_SQL_TEMPLATE, appName, message.toString()));
        }
        log.info(e.getMessage(), e);
    }

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql,
        String url) {
        if (isProd && EnvUtil.isAlarmService(this.appName) && alarmsql > 0) {
            long elapsedSenconds = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            if (elapsedSenconds > alarmsql) {
                if (applicationEventPublisher != null) {
                    applicationEventPublisher.publishEvent(new SlowSQLEvent(this, sql, elapsedSenconds));
                }
                AlarmUtil.alarmTeams(
                    String.format(SLOW_SQL_TEMPLATE, appName, elapsedSenconds, TrackingUtils.getTrace(), sql));
                doLog(String.format("SQL too Slow Warning cost:%s,sql:%s, uuid: %s", elapsed, sql,
                    TrackingUtils.getTrace()), category);
            }
        }

    }

    public static boolean startsWithIgnoreCaseAny(final CharSequence sequence, final CharSequence... searchStrings) {
        if (StringUtils.isEmpty(sequence) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (StringUtils.startsWithIgnoreCase(sequence, searchString)) {
                return true;
            }
        }
        return false;
    }

    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql,
        String url, Loggable loggable) {
        if (isProd && EnvUtil.isAlarmService(this.appName) && alarmsql > 0) {
            long elapsedSenconds = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
            if (elapsedSenconds >= alarmsql) {
                if (applicationEventPublisher != null) {
                    applicationEventPublisher.publishEvent(new SlowSQLEvent(this, sql, elapsedSenconds));
                }
                AlarmUtil.alarmTeams(
                    String.format(SLOW_SQL_TEMPLATE, appName, elapsedSenconds, TrackingUtils.getTrace(), sql));
                doLog(String.format("SQL too Slow Warning cost:%s,sql:%s, uuid: %s", elapsed, sql,
                    TrackingUtils.getTrace()), category);
            }
        }
    }

    @Override
    public void logText(String text) {
        log.info(text);
    }

    public static void main(String[] args) {
        String text = String.format(SLOW_SQL_TEMPLATE, "testService", "10", "123", "select * from dual");

        System.out.println(text);
    }

}
