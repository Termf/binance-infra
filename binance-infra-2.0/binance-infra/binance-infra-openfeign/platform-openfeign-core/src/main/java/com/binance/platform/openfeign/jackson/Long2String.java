 package com.binance.platform.openfeign.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
 @Retention(RetentionPolicy.RUNTIME)
 @JacksonAnnotation
 public @interface Long2String {


    /**
     * 三个参数：
     * 
     * <pre>
     *  mobile  只针对手机端做转化
     *  pc    只针对PC端做转化
     *  all   针对所有端做转化
     * </pre>
     * 
     */
    String clientType() default "all";
}
