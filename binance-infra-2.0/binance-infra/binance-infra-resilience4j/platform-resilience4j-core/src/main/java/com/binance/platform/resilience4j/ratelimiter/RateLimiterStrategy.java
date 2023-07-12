package com.binance.platform.resilience4j.ratelimiter;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public interface RateLimiterStrategy {

	/**
	 * 根据UserId或者根据IP限流
	 * 
	 * @return
	 */
	String strategy();

	/**
	 * 阈值刷新的时间
	 * 
	 * @return
	 */
	default Duration timeoutDuration() {
		return Duration.ofMillis(100);
	}

	/**
	 * 当存在多个策略时的顺序
	 * 
	 * @return
	 */
	default int order() {
		return 0;
	}

	default HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null && requestAttributes.getRequest() != null) {
			HttpServletRequest servletRequest = requestAttributes.getRequest();
			return servletRequest;
		} else {
			throw new RuntimeException("RequestContextHolder can not find, please add platform-starter-openfeign");
		}
	}
}
