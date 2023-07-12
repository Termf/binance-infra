package com.binance.master.utils;

import com.binance.master.error.BusinessException;
import com.binance.master.error.GeneralCode;
import org.springframework.util.ObjectUtils;

public final class Assert extends org.springframework.util.Assert {

    public static void notNull(Object object, GeneralCode code) {
        if (object == null) {
            throw new BusinessException(code);
        }
    }

    public static void notBlank(Object object, GeneralCode code) {
        if (ObjectUtils.isEmpty(object)) {
            throw new BusinessException(code);
        }
    }
}
