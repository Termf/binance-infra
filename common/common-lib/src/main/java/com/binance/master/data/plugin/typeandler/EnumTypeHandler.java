package com.binance.master.data.plugin.typeandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("all")
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final String[] methodNames = new String[] {"getCode"};
    private Method method;
    private Class<E> type;
    private final E[] enums;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
        for (String methodName : methodNames) {
            try {
                this.method = type.getMethod(methodName, null);
            } catch (NoSuchMethodException | SecurityException e) {
            }
        }
        if (this.method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.name());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                E e = null;
                for (E t : enums) {
                    Object retobj = this.method.invoke(t, null);
                    if (retobj.equals(code)) {
                        e = t;
                        break;
                    }
                }
                return e;
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            try {
                E e = null;
                for (E t : enums) {
                    Object retobj = this.method.invoke(t, null);
                    if (retobj.equals(code)) {
                        e = t;
                        break;
                    }
                }
                return e;
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
            }
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            try {
                E e = null;
                for (E t : enums) {
                    Object retobj = this.method.invoke(t, null);
                    if (retobj.equals(code)) {
                        e = t;
                        break;
                    }
                }
                return e;
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Cannot convert " + code + " to " + type.getSimpleName() + " by code value.", ex);
            }
        }
    }
}
