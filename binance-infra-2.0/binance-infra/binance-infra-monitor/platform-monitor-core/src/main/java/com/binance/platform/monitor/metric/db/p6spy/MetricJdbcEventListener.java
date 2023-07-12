package com.binance.platform.monitor.metric.db.p6spy;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.Loggable;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.SimpleJdbcEventListener;
import com.p6spy.engine.logging.Category;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

public class MetricJdbcEventListener extends SimpleJdbcEventListener {
    public static final Logger logger = LoggerFactory.getLogger(MetricJdbcEventListener.class);
    public static final String SQL_EXECUTE_TIME = "sql_execute_time";
    public static final MetricJdbcEventListener INSTANCE = new MetricJdbcEventListener();
    private static final Set<String> CALCULATE_METRCIS_SQL = Sets.newHashSet();
    private static final int DEFAULT_CALCULATE_METRCIS_SQL_COUNT = 1000;

    private static String getHost(String url) {
        if (StringUtils.isEmpty(url)) {
            return "none";
        }
        try {
            return url.split("\\?")[0];
        } catch (Throwable ex) {
            return url;
        }
    }

    private static String getOrDefault(Callable<String> call, String defaultValue) {
        try {
            return call.call();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static boolean needRecord(String sql) {
        if (StringUtils.equalsIgnoreCase(sql, "SELECT 1 FROM DUAL")) {
            return false;
        }
        if (StringUtils.startsWithIgnoreCase(sql, "insert") || StringUtils.startsWithIgnoreCase(sql, "update")
            || StringUtils.startsWithIgnoreCase(sql, "delete") || StringUtils.startsWithIgnoreCase(sql, "select")) {
            // 如果sql已经记录过了，则继续记录
            if (CALCULATE_METRCIS_SQL.contains(sql)) {
                return true;
            }
            // 如果sql记录数还未到最大限制，则继续记录
            if (CALCULATE_METRCIS_SQL.size() <= DEFAULT_CALCULATE_METRCIS_SQL_COUNT) {
                CALCULATE_METRCIS_SQL.add(sql);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAfterAnyAddBatch(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterAnyExecute(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.STATEMENT, e);
    }

    @Override
    public void onAfterCommit(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {}

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos,
        int[] updateCounts, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.BATCH, e);
    }

    @Override
    public void onAfterGetResultSet(StatementInformation statementInformation, long timeElapsedNanos, SQLException e) {
        record(statementInformation, timeElapsedNanos, Category.RESULTSET, e);
    }

    @Override
    public void onAfterRollback(ConnectionInformation connectionInformation, long timeElapsedNanos, SQLException e) {}

    protected void record(Loggable loggable, long timeElapsedNanos, Category category, SQLException e) {
        try {
            String sql = loggable.getSql().trim();
            if (needRecord(sql)) {
                Timer timer = Metrics.timer(SQL_EXECUTE_TIME, //
                    Tags.of(Tag.of("category", category.getName()), //
                        Tag.of("readonly",
                            getOrDefault(
                                () -> String.valueOf(loggable.getConnectionInformation().getConnection().isReadOnly()),
                                "error")), //
                        Tag.of("autocommit", getOrDefault(
                            () -> String.valueOf(loggable.getConnectionInformation().getConnection().getAutoCommit()),
                            "error")), //
                        Tag.of("statement", sql.length() > 500 ? sql.substring(0, 500) : sql), //
                        Tag.of("jdbc",
                            getHost(loggable.getConnectionInformation().getConnection().getMetaData().getURL()))));
                timer.record(timeElapsedNanos, TimeUnit.NANOSECONDS);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
