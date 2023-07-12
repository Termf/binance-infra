package com.binance.platform.monitor.metric;

import java.lang.reflect.Field;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.monitor.metric.db.hikaricp.MicrometerMetricsTrackerFactory;
import com.p6spy.engine.spy.P6DataSource;
import com.zaxxer.hikari.HikariDataSource;

import io.micrometer.core.instrument.Metrics;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

public class DataSourceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private final CustomizerTag tag;

    public DataSourceBeanPostProcessor(CustomizerTag tag) {
        this.tag = tag;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (DataSource.class.isAssignableFrom(bean.getClass()) && !(bean instanceof P6DataSource)) {
            if (bean.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                com.zaxxer.hikari.HikariDataSource hikariDataSource = (com.zaxxer.hikari.HikariDataSource)bean;
                if (hikariDataSource.getMetricsTrackerFactory() == null) {
                    hikariDataSource
                        .setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory(Metrics.globalRegistry, tag));
                }
                String jdbcUrl = hikariDataSource.getJdbcUrl();
                if (!StringUtils.containsIgnoreCase(jdbcUrl, "p6spy")) {
                    Field field = ReflectionUtils.findField(HikariDataSource.class, "sealed");
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, hikariDataSource, false);
                    hikariDataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
                    jdbcUrl = "jdbc:p6spy:" + StringUtils.substringAfter(jdbcUrl, "jdbc:");
                    hikariDataSource.setJdbcUrl(jdbcUrl);
                }
                return hikariDataSource;
            } else {
                if (bean.getClass().getName()
                    .equals("io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource")) {
                    ShardingDataSource datasource = (ShardingDataSource)bean;
                    Map<String, DataSource> dataSourceMap = datasource.getDataSourceMap();
                    dataSourceMap.forEach((k, v) -> {
                        if (v.getClass().getName().equals("com.zaxxer.hikari.HikariDataSource")) {
                            com.zaxxer.hikari.HikariDataSource hikaridatasource = (com.zaxxer.hikari.HikariDataSource)v;
                            Field field = ReflectionUtils.findField(HikariDataSource.class, "sealed");
                            ReflectionUtils.makeAccessible(field);
                            ReflectionUtils.setField(field, hikaridatasource, false);
                            if (hikaridatasource.getMetricsTrackerFactory() == null) {
                                hikaridatasource.setMetricsTrackerFactory(
                                    new MicrometerMetricsTrackerFactory(Metrics.globalRegistry, tag));
                            }
                        }
                    });

                }
                P6DataSource dataSource = new P6DataSource((DataSource)bean);
                dataSource.setRealDataSource(beanName);
                return dataSource;
            }
        }
        return bean;
    }

}
