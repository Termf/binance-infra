package com.binance.platform.mybatis.mybatisplus.injector.methods;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.binance.platform.mybatis.mybatisplus.injector.MySqlMethod;

public class InsertBatch extends AbstractMethod {

	private final DataSource dataSource;

	public InsertBatch(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
		KeyGenerator keyGenerator = new NoKeyGenerator();
		StringBuilder fieldBuilder = new StringBuilder();
		MySqlMethod sqlMethod = MySqlMethod.INSERT_BATCH_MYSQL;
		String dbUrl = null;
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			dbUrl = connection.getMetaData().getURL();
		} catch (Throwable e) {
		} finally {
			if (connection != null) {
				DataSourceUtils.releaseConnection(connection, dataSource);
			}
		}
		StringBuilder placeholderBuilder = new StringBuilder();
		DbType dbType = StringUtils.isBlank(dbUrl) ? DbType.MYSQL : JdbcUtils.getDbType(dbUrl);
		if (DbType.ORACLE == dbType) {
			sqlMethod = MySqlMethod.INSERT_BATCH_ORACLE;
			placeholderBuilder.append("\n<trim prefix=\"(SELECT \" suffix=\" FROM DUAL)\" suffixOverrides=\",\">\n");
		} else {
			placeholderBuilder.append("\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
		}
		fieldBuilder.append("\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
		String keyProperty = null;
		String keyColumn = null;
		if (table.getIdType() == IdType.AUTO) {
			/* 自增主键 */
			keyGenerator = new Jdbc3KeyGenerator();
			keyProperty = table.getKeyProperty();
			keyColumn = table.getKeyColumn();
		} else {
			/* 用户输入自定义ID */
			fieldBuilder.append(table.getKeyColumn()).append(",");
			placeholderBuilder.append("#{item.").append(table.getKeyProperty()).append("},");
		}
		List<TableFieldInfo> fieldList = table.getFieldList();
		for (TableFieldInfo fieldInfo : fieldList) {
			fieldBuilder.append(fieldInfo.getColumn()).append(",");
			placeholderBuilder.append("#{item.").append(fieldInfo.getEl()).append("},");
		}
		fieldBuilder.append("\n</trim>");
		placeholderBuilder.append("\n</trim>");
		String sql = String.format(sqlMethod.getSql(), table.getTableName(), fieldBuilder.toString(),
				placeholderBuilder.toString());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

}
