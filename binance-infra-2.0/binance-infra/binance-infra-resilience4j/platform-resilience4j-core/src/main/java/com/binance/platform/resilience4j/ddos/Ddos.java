package com.binance.platform.resilience4j.ddos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Documented
/**
 * DDOS拦截注解，添加在controller或者其handler方法上。
 *
 * @author Li Fenggang Date: 2020/8/15 5:06 下午
 */
public @interface Ddos {

	long DEFAULT_ELAPSE_TIMEOUT = 100L;
	String DEFAULT_LIMIT_FOR_PERIOD = "1000";
	String DEFAULT_LIMIT_REFRESH_PERIOD = "1";

	/**
	 * 时间窗口的限制频次，默认值为{@value #DEFAULT_LIMIT_FOR_PERIOD}。
	 * <p>
	 * 支持使用spring占位符配置格式：{@code ${sample.property:default}}
	 * </p>
	 * 
	 * @return
	 */
	String limitForPeriod() default DEFAULT_LIMIT_FOR_PERIOD;

	/**
	 * 时间窗口的大小（秒）。默认值为{@value #DEFAULT_LIMIT_REFRESH_PERIOD}。
	 * <p>
	 * 支持使用spring占位符配置格式：{@code ${sample.property:default}}
	 * </p>
	 * 
	 * @return
	 */
	String limitRefreshPeriod() default DEFAULT_LIMIT_REFRESH_PERIOD;

	/**
	 * 判定超过时间窗口约束的等待超时时间（毫秒），默认值为{@value #DEFAULT_ELAPSE_TIMEOUT}。
	 * 这个属性请谨慎设置，请求数超过时间窗口上限时，它会阻塞线程相应的时间。
	 * 
	 * @return
	 */
	long timeoutDuration() default DEFAULT_ELAPSE_TIMEOUT;

	/**
	 * 自定义ddos策略
	 * 
	 * @return
	 */
	Class<? extends DdosStrategy> strategy() default SystemDefaultStrategy.class;

}
