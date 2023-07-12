package com.binance.platform.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.platform.mybatis.handler.encrypt.CryptType;
import com.binance.platform.mybatis.handler.encrypt.StringEncryptor;

public class EncryptedColumnHandler extends BaseTypeHandler<CryptType> {
    public static final Logger logger = LoggerFactory.getLogger(EncryptedColumnHandler.class);

    private CryptType get(String value) {
        if (null == value) {
            return null;
        }
        try {
            return CryptType.create(StringEncryptor.getStringEncryptor().decrypt(value));
        } catch (Throwable ex) {
            logger.error("Can not decrypt value {}", value);
            return CryptType.create(value);
        }
    }

    @Override
    public CryptType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String ret = cs.getString(columnIndex);
        return get(ret);
    }

    @Override
    public CryptType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String ret = rs.getString(columnIndex);
        return get(ret);
    }

    @Override
    public CryptType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String ret = rs.getString(columnName);
        return get(ret);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CryptType parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, StringEncryptor.getStringEncryptor().encrypt(parameter.getValue()));
    }
}
