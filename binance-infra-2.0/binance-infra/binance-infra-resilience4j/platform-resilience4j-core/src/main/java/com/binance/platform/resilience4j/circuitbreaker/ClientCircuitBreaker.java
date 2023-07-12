package com.binance.platform.resilience4j.circuitbreaker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface ClientCircuitBreaker {

    /**
     *
     * @return
     */
    String failureRateThreshold();

    /**
     *
     * @return
     */
    String waitDurationInOpenState();

    /**
     *
     * @return
     */
    String ringBufferSizeInHalfOpenState();

    /**
     *
     * @return
     */
    String ringBufferSizeInClosedState();

    /**
     *
     * @return
     */
    Class<?> fallback();

    /**
     * ignoreExceptions
     * 
     * @return
     */
    Class<? extends Throwable>[] ignoreExceptions();

}
