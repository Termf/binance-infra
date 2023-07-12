package com.binance.platform.resilience4j.ratelimiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: sait xuc
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface BnRateLimiter {

    String timeoutDuration() default "5";//单位妙， 默认为5秒

    String limitRefreshPeriod() default "1";//单位为秒， 默认为1秒

    String limitForPeriod()default "100";//单位个数， 默认100

    String fallbackMethod() default "";

}
