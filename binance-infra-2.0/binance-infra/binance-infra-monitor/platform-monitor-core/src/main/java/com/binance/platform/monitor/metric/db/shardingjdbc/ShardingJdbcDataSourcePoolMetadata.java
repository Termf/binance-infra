package com.binance.platform.monitor.metric.db.shardingjdbc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

import com.binance.platform.monitor.metric.CustomizerTag;

import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

public class ShardingJdbcDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<ShardingDataSource> {

    public ShardingJdbcDataSourcePoolMetadata(ShardingDataSource dataSource, CustomizerTag tag) {
        super(dataSource);
    }

    @Override
    public Integer getActive() {
        AtomicInteger active = new AtomicInteger(0);
        getDataSource().getDataSourceMap().forEach((k, v) -> {
            if (v.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                com.zaxxer.hikari.HikariDataSource hikaridatasource = (com.zaxxer.hikari.HikariDataSource)v;
                int t = ((com.zaxxer.hikari.pool.HikariPool)new DirectFieldAccessor(hikaridatasource)
                    .getPropertyValue("pool")).getActiveConnections();
                active.addAndGet(t);
            }
        });
        return active.intValue();
    }

    @Override
    public Integer getMax() {
        AtomicInteger max = new AtomicInteger(0);
        getDataSource().getDataSourceMap().forEach((k, v) -> {
            if (v.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                com.zaxxer.hikari.HikariDataSource hikaridatasource = (com.zaxxer.hikari.HikariDataSource)v;
                max.addAndGet(hikaridatasource.getMaximumPoolSize());
            }
        });
        return max.intValue();
    }

    @Override
    public Integer getMin() {
        AtomicInteger min = new AtomicInteger(0);
        getDataSource().getDataSourceMap().forEach((k, v) -> {
            if (v.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                com.zaxxer.hikari.HikariDataSource hikaridatasource = (com.zaxxer.hikari.HikariDataSource)v;
                min.addAndGet(hikaridatasource.getMinimumIdle());
            }
        });
        return min.intValue();
    }

    @Override
    public String getValidationQuery() {
        return "SELECT 1 FROM DUAL";
    }

    @Override
    public Boolean getDefaultAutoCommit() {
        AtomicBoolean autoCommit = new AtomicBoolean(false);
        getDataSource().getDataSourceMap().forEach((k, v) -> {
            if (v.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                com.zaxxer.hikari.HikariDataSource hikaridatasource = (com.zaxxer.hikari.HikariDataSource)v;
                autoCommit.getAndSet(hikaridatasource.isAutoCommit());
            }
        });
        return autoCommit.get();
    }

}
