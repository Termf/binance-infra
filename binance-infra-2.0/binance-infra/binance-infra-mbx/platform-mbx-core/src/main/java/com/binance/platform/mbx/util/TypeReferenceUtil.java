package com.binance.platform.mbx.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Li Fenggang
 * Date: 2020/7/19 6:16 上午
 */
public class TypeReferenceUtil {
    public static Type getRawType(TypeReference typeReference) {
        if (typeReference != null) {
            Type type = typeReference.getType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)type;
                Type rawType = parameterizedType.getRawType();
                return rawType;
            }
        }
        return null;
    }
}
