package com.binance.platform.monitor.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.binance.platform.monitor.metric.CustomizerTag;
import com.binance.platform.monitor.metric.db.MysqlStatusBinder;
import com.binance.platform.monitor.metric.db.druid.DruidDataSourcePoolMetadata;
import com.binance.platform.monitor.metric.db.shardingjdbc.ShardingJdbcDataSourcePoolMetadata;
import com.p6spy.engine.spy.P6DataSource;

import io.micrometer.core.instrument.Metrics;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

@AutoConfigureAfter({DataSourceAutoConfiguration.class, MicrometerAutoConfiguration.class})
@Configuration
public class DataSourceMetricAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceMetricAutoConfiguration.class);

    @Configuration
    @ConditionalOnBean(DataSource.class)
    protected class P6DataSourceAutoConfiguration {

        @Autowired
        Map<String, DataSource> dataSourceMap;

        @Autowired
        CustomizerTag tag;

        @Value("${monitor.sql.mysql.variable:Questions,Com_commit,Com_rollback,Table_locks_waited}")
        private String mysqlVariable;

        @PostConstruct
        public void init() {
            dataSourceMap.forEach((name, dataSource) -> {
                new MysqlStatusBinder(dataSource, name, tag.getTags(),
                    Arrays.asList(mysqlVariable.split(",")).stream().collect(Collectors.toSet()))
                        .bindTo(Metrics.globalRegistry);
            });
        }

        @Bean
        @ConditionalOnBean(DataSourcePoolMetadataProvider.class)
        public DataSourcePoolMetadataProvider
            p6DataSourcePoolMetadataProvider(List<DataSourcePoolMetadataProvider> allDataSourcePoolMetadataProvider) {
            return new DataSourcePoolMetadataProvider() {
                @Override
                public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
                    dataSource = getRealDataSource(dataSource);
                    for (DataSourcePoolMetadataProvider d : allDataSourcePoolMetadataProvider) {
                        DataSourcePoolMetadata dataSourcePoolMetadata = d.getDataSourcePoolMetadata(dataSource);
                        if (null != dataSourcePoolMetadata) {
                            return dataSourcePoolMetadata;
                        }
                    }
                    return null;
                }
            };
        }

    }

    private static DataSource getRealDataSource(DataSource dataSource) {
        if (dataSource instanceof P6DataSource) {
            Field realDataSourceField = ReflectionUtils.findField(P6DataSource.class, "realDataSource");
            ReflectionUtils.makeAccessible(realDataSourceField);
            try {
                CommonDataSource ds = (CommonDataSource)realDataSourceField.get(dataSource);
                if (ds instanceof DataSource) {
                    return (DataSource)ds;
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return dataSource;

    }

    @Configuration
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnClass(DruidDataSource.class)
    static class DruidDataSourceMetricAutoConfiguration {
        @Bean
        public DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
            return new DataSourcePoolMetadataProvider() {

                @Override
                public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
                    DataSource realdataSource = getRealDataSource(dataSource);
                    if (realdataSource instanceof com.alibaba.druid.pool.DruidDataSource) {
                        return new DruidDataSourcePoolMetadata((com.alibaba.druid.pool.DruidDataSource)realdataSource);
                    } else {
                        return null;
                    }
                }

            };
        }
    }

    @Configuration
    @ConditionalOnClass(ShardingDataSource.class)
    @ConditionalOnBean(DataSource.class)
    static class ShardingDataSourceMetricAutoConfiguration {

        @Autowired
        CustomizerTag tag;

        @Bean
        public DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
            return new DataSourcePoolMetadataProvider() {

                @Override
                public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
                    DataSource realdataSource = getRealDataSource(dataSource);
                    if (realdataSource instanceof io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource) {
                        return new ShardingJdbcDataSourcePoolMetadata(
                            (io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource)realdataSource,
                            tag);
                    } else {
                        return null;
                    }
                }

            };
        }

    }
}
