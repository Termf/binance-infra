package com.binance.platform.resilience4j.ratelimiter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.binance.master.constant.Constant;

public class UserIdRateLimiterStrategy implements RateLimiterStrategy {

	@Override
	public String strategy() {
		HttpServletRequest servletRequest = getHttpServletRequest();
		String userId = servletRequest.getHeader(Constant.HEADER_USER_ID);
		return userId != null ? userId : StringUtils.EMPTY;

	}

}
