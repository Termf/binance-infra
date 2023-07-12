package com.binance.master.old.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.binance.master.data.DBConfig;


@Configuration
@PropertySource("classpath:old/db-${spring.profiles.active}.properties")
@MapperScan(basePackages = "com.binance.master.old.data", annotationClass = OldDB.class,
        sqlSessionFactoryRef = "oldSqlSessionFactory")
public class OldDBConfig extends DBConfig {

    @Bean(name = "oldDataSource")
    @Override
    public DataSource dataSource() throws SQLException {
        JSONObject datas = JSON.parseObject(super.env.getProperty("old.hikaricp.data", String.class));
        return this.dataSource(datas, null);
    }

    @Bean(name = OldDB.TRANSACTION)
    @Override
    public DataSourceTransactionManager transactionManager(@Qualifier("oldDataSource") DataSource dataSource) {
        return super.transactionManager(dataSource);
    }

    @Bean(name = "oldSqlSessionFactory")
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier("oldDataSource") DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource);
    }

}
