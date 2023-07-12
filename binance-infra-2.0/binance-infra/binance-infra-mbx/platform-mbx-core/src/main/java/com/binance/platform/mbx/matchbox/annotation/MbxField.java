package com.binance.platform.mbx.matchbox.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Match box请求中的字段关联。不添加这个关联，代表使用字段名。
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MbxField {
    /**
     * match box请求中的字段名
     * @return
     */
    String value();
}
