package com.binance.platform.sapi.config;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
public class BodyMetaData {
    private final int paramIndex;
    private final List<Field> fieldList;

    public BodyMetaData(int paramIndex, List<Field> fieldList) {
        this.paramIndex = paramIndex;
        this.fieldList = fieldList;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public static BodyMetaData build(int paramIndex, Class<?> paramClass) {
        Field[] declaredFields = paramClass.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(declaredFields.length);
        for (Field declaredField : declaredFields) {
            ReflectionUtils.makeAccessible(declaredField);
            fieldList.add(declaredField);
        }
        return new BodyMetaData(paramIndex, fieldList);
    }
}
