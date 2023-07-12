package com.binance.platform.mybatis.mybatisplus.injector.methods;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.binance.platform.mybatis.mybatisplus.injector.MySqlMethod;

public class UpdateBatchById extends AbstractMethod {

	private final DataSource dataSource;

	public UpdateBatchById(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
		StringBuilder set = new StringBuilder();
		set.append("<trim prefix=\"SET\" suffixOverrides=\",\">\n");
		MySqlMethod sqlMethod = MySqlMethod.UPDATE_BATCH_BY_ID_MYSQL;
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
		if (DbType.ORACLE == dbType) {
			sqlMethod = MySqlMethod.UPDATE_BATCH_BY_ID_ORACLE;
			List<TableFieldInfo> fieldList = table.getFieldList();
			for (TableFieldInfo fieldInfo : fieldList) {
				set.append(fieldInfo.getColumn()).append("=#{item.").append(fieldInfo.getEl()).append("},");
			}
		} else if (DbType.MYSQL == dbType || DbType.H2 == dbType) {
			List<TableFieldInfo> fieldList = table.getFieldList();
			for (TableFieldInfo fieldInfo : fieldList) {
				set.append("\n<trim prefix=\"").append(fieldInfo.getColumn()).append("=CASE ");
				set.append(table.getKeyColumn()).append("\" suffix=\"END,\">");
				set.append("\n<foreach collection=\"list\" item=\"i\" index=\"index\">");
				set.append(convertIfTag(fieldInfo, "i.", false));
				set.append("\nWHEN ").append("#{i.").append(table.getKeyProperty());
				set.append("} THEN #{i.").append(fieldInfo.getEl()).append("}");
				set.append(convertIfTag(fieldInfo, true));
				set.append("\n</foreach>");
				set.append("\n</trim>");
			}
		} else {
			throw new UnsupportedOperationException("DbType not supported" + dbType);
		}
		set.append("\n</trim>");
		String sql = String.format(sqlMethod.getSql(), table.getTableName(), set.toString(), table.getKeyColumn(),
				table.getKeyProperty());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
	}

	protected String convertIfTag(SqlCommandType sqlCommandType, TableFieldInfo fieldInfo, String prefix,
			boolean colse) {
		/* 前缀处理 */
		String property = fieldInfo.getProperty();
		if (null != prefix) {
			property = prefix + property;
		}
		/* 判断策略 */
		if (sqlCommandType == SqlCommandType.INSERT && fieldInfo.getUpdateStrategy() == FieldStrategy.DEFAULT) {
			return "";
		}
		if (fieldInfo.getUpdateStrategy() == FieldStrategy.IGNORED) {
			return "";
		} else if (fieldInfo.getUpdateStrategy() == FieldStrategy.NOT_EMPTY) {
			if (colse) {
				return "</if>";
			} else {
				return String.format("\n\t<if test=\"%s!=null and %s!=''\">", property, property);
			}
		} else {
			// FieldStrategy.NOT_NULL
			if (colse) {
				return "</if>";
			} else {
				return String.format("\n\t<if test=\"%s!=null\">", property);
			}
		}
	}

	protected String convertIfTag(TableFieldInfo fieldInfo, String prefix, boolean colse) {
		return convertIfTag(SqlCommandType.UNKNOWN, fieldInfo, prefix, colse);
	}

	protected String convertIfTag(TableFieldInfo fieldInfo, boolean colse) {
		return convertIfTag(fieldInfo, null, colse);
	}
}
