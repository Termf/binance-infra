package com.binance.platform.mybatis.mybatisplus.injector.methods;

import static java.util.stream.Collectors.joining;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.binance.platform.mybatis.mybatisplus.injector.MySqlMethod;

public class SaveOrUpdate extends AbstractMethod {

	private final DataSource dataSource;

	public SaveOrUpdate(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		MySqlMethod sqlMethod = MySqlMethod.SAVE_OR_UPDATE_MYSQL;
		KeyGenerator keyGenerator = new NoKeyGenerator();
		String keyProperty = null;
		String keyColumn = null;
		// 表包含主键处理逻辑,如果不包含主键当普通字段处理
		if (StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				/** 自增主键 */
				keyGenerator = new Jdbc3KeyGenerator();
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			} else {
				if (null != tableInfo.getKeySequence()) {
					keyGenerator = TableInfoHelper.genKeyGenerator(tableInfo, builderAssistant, sqlMethod.getMethod(),
							languageDriver);
					keyProperty = tableInfo.getKeyProperty();
					keyColumn = tableInfo.getKeyColumn();
				}
			}
		}

		String columnScript = SqlScriptUtils
				.convertTrim(SqlScriptUtils.convertIf(keyColumn + ",", String.format("%s != null", keyColumn), true)
						+ tableInfo.getAllInsertSqlColumnMaybeIf(), LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
		String valuesScript = SqlScriptUtils.convertTrim(
				SqlScriptUtils.convertIf("#{" + keyProperty + "},", String.format("%s != null", keyProperty), true)
						+ tableInfo.getAllInsertSqlPropertyMaybeIf(null),
				LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);

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
		DbType dbType = StringUtils.isBlank(dbUrl) ? DbType.MYSQL : JdbcUtils.getDbType(dbUrl);
		String updateScript = null;
		if (sqlMethod.getSupportDbTypes().contains(dbType)) {
			updateScript = SqlScriptUtils.convertTrim(
					tableInfo.getFieldList().stream().map(i -> i.getSqlSet("")).collect(joining(NEWLINE)), null, null,
					null, COMMA);
		} else {
			throw new UnsupportedOperationException("DbType not supported : " + dbType);
		}
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript,
				updateScript);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator,
				keyProperty, keyColumn);
	}

	public static void main(String[] args) {
		System.out.println(SqlScriptUtils.convertIf("id", String.format("%s != null", "id"), false));
		System.out.println(SqlScriptUtils.convertIf("#{id}", String.format("%s != null", "id"), false));
	}
}
