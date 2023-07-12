package com.binance.platform.resilience4j.ratelimiter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface ServerRateLimiter {

	/**
	 * 限制策略，按IP限流还是按UserId限流
	 * 
	 * @return
	 */
	Class<? extends RateLimiterStrategy>[] strategy();

	/**
	 * 限制频次
	 * 
	 * @return
	 */
	String limitForPeriod();

	/**
	 * 冷却时间,单位为秒
	 * 
	 * @return
	 */
	String limitRefreshPeriod();

	/**
	 * 是否使用redis
	 * 
	 * @return
	 */
	boolean useRedis();

}
