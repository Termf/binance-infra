package com.binance.platform.resilience4j.timeLimiter;

import java.lang.annotation.*;

/**
 * author: sait xuc
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface ClientTimeLimiter {

    /**
     *
     * @return
     */
    String timeoutDuration();//超时时间


    boolean cancelRunningFuture() default true;//是否Cancel Future

}
