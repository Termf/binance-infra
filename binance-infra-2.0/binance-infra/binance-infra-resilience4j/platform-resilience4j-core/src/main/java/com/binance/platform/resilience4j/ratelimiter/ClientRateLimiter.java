package com.binance.platform.resilience4j.ratelimiter;

import java.lang.annotation.*;

/**
 * author: sait xuc
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface ClientRateLimiter {

    /**
     *
     * @return
     */
    String limitForPeriod();

    /**
     *
     * @return
     */
    String limitRefreshPeriod();

    /**
     *
     * @return
     */
    String timeoutDuration() default "1";

}
