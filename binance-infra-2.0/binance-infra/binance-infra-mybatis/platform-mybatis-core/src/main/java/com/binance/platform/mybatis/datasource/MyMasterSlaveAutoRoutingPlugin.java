package com.binance.platform.mybatis.datasource;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

import com.baomidou.dynamic.datasource.toolkit.DdConstants;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

/**
 * Master-slave Separation Plugin with mybatis
 *
 * @author TaoYu
 * @since 2.5.1
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MyMasterSlaveAutoRoutingPlugin implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		boolean empty = true;
		try {
			empty = StringUtils.isEmpty(DynamicDataSourceContextHolder.peek());
			if (empty) {
				DynamicDataSourceContextHolder.push(getDataSource(ms));
			}
			return invocation.proceed();
		} finally {
			if (empty) {
				DynamicDataSourceContextHolder.clear();
			}
		}
	}

	/**
	 * 获取动态数据源名称，重写注入 DbHealthIndicator 支持数据源健康状况判断选择
	 *
	 * @param mappedStatement mybatis MappedStatement
	 */
	public String getDataSource(MappedStatement mappedStatement) {
		return SqlCommandType.SELECT == mappedStatement.getSqlCommandType() ? DdConstants.SLAVE : DdConstants.MASTER;
	}

	@Override
	public Object plugin(Object target) {
		return target instanceof Executor ? Plugin.wrap(target, this) : target;
	}

	@Override
	public void setProperties(Properties properties) {
	}
}