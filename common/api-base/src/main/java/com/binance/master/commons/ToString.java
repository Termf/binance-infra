package com.binance.master.commons;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ToStirng组件
 *
 * @author bijie-wang
 */
public class ToString implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5007798762241615802L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


}
