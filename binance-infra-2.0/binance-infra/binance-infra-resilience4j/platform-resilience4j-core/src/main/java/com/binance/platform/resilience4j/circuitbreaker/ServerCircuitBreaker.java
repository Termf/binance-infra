package com.binance.platform.resilience4j.circuitbreaker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface ServerCircuitBreaker {

    /**
     * 失败率阈值
     * 
     * @return
     */
    String failureRateThreshold();

    /**
     * 用来指定断路器从OPEN到HALF_OPEN状态等待的时长
     * 
     * @return
     */
    String waitDurationInOpenState();

    /**
     * 设置当断路器处于HALF_OPEN状态下的ringbuffer的大小，它存储了最近一段时间请求的成功失败状态
     * 
     * @return
     */
    String ringBufferSizeInHalfOpenState();

    /**
     * 设置当断路器处于CLOSED状态下的ringbuffer的大小，它存储了最近一段时间请求的成功失败状态
     * 
     * @return
     */
    String ringBufferSizeInClosedState();

    /**
     * ignoreExceptions
     * 
     * @return
     */
    Class<? extends Throwable>[] ignoreExceptions();
}
