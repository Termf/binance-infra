package com.binance.platform.monitor.metric.db.p6spy;

import org.springframework.context.ApplicationEvent;

public class SlowSQLEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final String sql;

    private final long costTimeBySencond;

    public SlowSQLEvent(Object source, String sql, long costTimeBySencond) {
        super(source);
        this.sql = sql;
        this.costTimeBySencond = costTimeBySencond;
    }

    public String getSql() {
        return sql;
    }

    public long getCostTimeBySencond() {
        return costTimeBySencond;
    }

}
