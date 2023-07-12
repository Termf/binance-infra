package com.binance.master.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;

/**
 * 启动skywalking的traceId日志打印，日志的log pattern中添加 [%X{traceId}]</br>
 * 由request请求触发保存跨线程的traceId,注解内部属性进一步限定触发条件
 * 
 * @author zhangwenhui
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface EnableSKYWalkingTraceId {
    /**
     * 针对指定的url pattern 默认 /*
     * 
     * @return
     *
     */
    String[] urlPattern() default "/*";

    /**
     * 针对指定的Http method，默认</br>
     * HttpMethod.GET </br>
     * HttpMethod.POST </br>
     * HttpMethod.PUT,</br>
     * HttpMethod.PATCH</br>
     * HttpMethod.DELETE</br>
     * 
     * @return
     */
    HttpMethod[] httpMethod() default {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE};

    /**
     * 针对特定ContentType
     * 
     * @return
     */
    String[] requestContentType() default {};
}
