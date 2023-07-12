package com.binance.platform.mybatis.mybatisplus;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.binance.platform.mybatis.mybatisplus.injector.MySqlInjector;

@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, BaseMapper.class})
@AutoConfigureAfter(name = {"com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration",
    "com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration"})
public class MybatisplusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public ISqlInjector sqlInjector(List<DataSource> dataSource) {
        return new MySqlInjector(dataSource);
    }

    @Value("${mybatis-plus.typeEnumsPackage:}")
    private String typeEnumsPackage;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CustomizeSqlSessionFactory customizeSqlSessionFactory(List<SqlSessionFactory> sqlSessionFactory,
        ApplicationContext applicationContext) {
        return new CustomizeSqlSessionFactory(typeEnumsPackage, sqlSessionFactory, applicationContext);
    }

}
