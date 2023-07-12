package com.binance.platform.mybatis.datasource;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.binance.platform.mybatis.DynamicDatasourceConnectionProvider;

public class MyDynamicDataSourceContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDynamicDataSourceContextInitializer.class);

    public static final String WRITE_DATASOURCE_NAME = "master";

    public static final String READ_DATASOURCE_NAME = "slave";

    private void buildDynamicDataSourceProperties(DynamicDataSourceProperties dynamicDataSourceProperties,
        DynamicDatasourceConnectionProvider dynamicDatasourceConnectionDataProvider) {
        dynamicDataSourceProperties.setPrimary(WRITE_DATASOURCE_NAME);
        dynamicDataSourceProperties.setStrict(false);
        dynamicDataSourceProperties.getDatasource().put(WRITE_DATASOURCE_NAME,
            buildWriteDataSourceProperty(dynamicDatasourceConnectionDataProvider));
        dynamicDataSourceProperties.getDatasource().put(READ_DATASOURCE_NAME,
            buildReadDataSourceProperty(dynamicDatasourceConnectionDataProvider));
    }

    private DataSourceProperty
        buildWriteDataSourceProperty(DynamicDatasourceConnectionProvider dynamicDatasourceConnectionDataProvider) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(dynamicDatasourceConnectionDataProvider.writeJdbcUrl());
        dataSourceProperty.setUsername(dynamicDatasourceConnectionDataProvider.writeUserName());
        dataSourceProperty.setPassword(dynamicDatasourceConnectionDataProvider.writePassoword());
        dataSourceProperty.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSourceProperty.setType(com.zaxxer.hikari.HikariDataSource.class);
        return dataSourceProperty;
    }

    private DataSourceProperty
        buildReadDataSourceProperty(DynamicDatasourceConnectionProvider dynamicDatasourceConnectionDataProvider) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl(dynamicDatasourceConnectionDataProvider.readJdbcUrl());
        dataSourceProperty.setUsername(dynamicDatasourceConnectionDataProvider.readUserName());
        dataSourceProperty.setPassword(dynamicDatasourceConnectionDataProvider.readPassoword());
        dataSourceProperty.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSourceProperty.setType(com.zaxxer.hikari.HikariDataSource.class);
        return dataSourceProperty;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {

            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (StringUtils.equalsIgnoreCase(bean.getClass().getName(),
                    "com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties")) {
                    /**
                     * FixBug on 2020/6/17 dynamicDataSource的Bug，没有对Executor的 <E> List<E> query(MappedStatement ms,
                     * Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql
                     * boundSql) throws SQLException; 做拦截，这个需要添加一个Mybatis的Interceptor来修复
                     * 
                     */
                    applicationContext.getBeanFactory().registerSingleton(
                        MyMasterSlaveAutoRoutingPlugin.class.getSimpleName(), new MyMasterSlaveAutoRoutingPlugin());
                    /**
                     * 用DynamicDatasourceConnectionProvider的值来覆盖DynamicDataSourceProperties的值
                     */
                    DynamicDataSourceProperties dynamicDataSourceProperties = (DynamicDataSourceProperties)bean;
                    Map<String, DynamicDatasourceConnectionProvider> dynamicDatasourceConnectionDataProviders =
                        applicationContext.getBeansOfType(DynamicDatasourceConnectionProvider.class);
                    // 如果有DynamicDatasourceConnectionProvider，则覆盖dynamicDataSourceProperties的值
                    if (dynamicDatasourceConnectionDataProviders != null
                        && dynamicDatasourceConnectionDataProviders.size() == 1) {
                        buildDynamicDataSourceProperties(dynamicDataSourceProperties,
                            dynamicDatasourceConnectionDataProviders.values().iterator().next());
                        return dynamicDataSourceProperties;
                    }
                    // 如果没有DynamicDatasourceConnectionProvider，直接使用原生的，也许是应用使用了Yml的配置
                    else if (dynamicDatasourceConnectionDataProviders == null
                        || dynamicDatasourceConnectionDataProviders.size() == 0) {
                        return dynamicDataSourceProperties;
                    }
                    // 如果有多个DynamicDatasourceConnectionProvider，也直接使用原生的，因为多个不知道使用那个DynamicDatasourceConnectionProvider
                    else {
                        LOGGER.warn(
                            "has found too many DynamicDatasourceConnectionProvider or no have no DynamicDatasourceConnectionProvider in you application, Please change it");
                        return dynamicDataSourceProperties;
                    }

                }
                return bean;

            }

        });

    }

}
