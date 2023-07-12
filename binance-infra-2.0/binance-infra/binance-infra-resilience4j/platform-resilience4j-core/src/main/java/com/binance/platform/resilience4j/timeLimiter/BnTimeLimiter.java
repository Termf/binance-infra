package com.binance.platform.resilience4j.timeLimiter;

import java.lang.annotation.*;

/**
 * author: sait xuc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface BnTimeLimiter {

    /**
     *
     * @return
     */
    String timeoutDuration();//超时时间 毫秒

    /**
     *
     * @return
     */
    String cancelRunningFuture() default "true";//是否Cancel Future

    /**
     *
     * @return
     */
    String fallbackMethod() default "";

}
