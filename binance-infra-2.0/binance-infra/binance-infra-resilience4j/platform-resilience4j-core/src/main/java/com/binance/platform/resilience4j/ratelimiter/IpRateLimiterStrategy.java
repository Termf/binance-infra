package com.binance.platform.resilience4j.ratelimiter;

import javax.servlet.http.HttpServletRequest;

import com.binance.master.utils.RequestIpUtil;

public class IpRateLimiterStrategy implements RateLimiterStrategy {

	@Override
	public String strategy() {
		HttpServletRequest servletRequest = getHttpServletRequest();
		return RequestIpUtil.getIpAddress(servletRequest);
	}

}
