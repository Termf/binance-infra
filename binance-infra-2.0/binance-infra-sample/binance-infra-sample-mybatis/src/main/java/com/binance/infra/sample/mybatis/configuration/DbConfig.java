package com.binance.infra.sample.mybatis.configuration;

import com.binance.platform.mybatis.DynamicDatasourceConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
    @Bean
    public DynamicDatasourceConnectionProvider dynamicDatasourceConnectionDataProvider() {

        return new DynamicDatasourceConnectionProvider() {

            @Override
            public String writeJdbcUrl() {
                return "jdbc:mysql://localhost:3306/exampledb?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false";
            }

            @Override
            public String writeUserName() {
                return "root";
            }

            @Override
            public String writePassoword() {
                return "letmein";
            }

            @Override
            public String readJdbcUrl() {
                return "jdbc:mysql://localhost:3306/exampledb?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false";
            }

            @Override
            public String readUserName() {
                return "root";
            }

            @Override
            public String readPassoword() {
                return "letmein";
            }

        };
    }
}
