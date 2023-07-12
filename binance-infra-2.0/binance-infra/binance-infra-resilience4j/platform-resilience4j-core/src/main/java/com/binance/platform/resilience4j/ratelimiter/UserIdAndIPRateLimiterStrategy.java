package com.binance.platform.resilience4j.ratelimiter;

import javax.servlet.http.HttpServletRequest;

import com.binance.master.constant.Constant;
import com.binance.master.utils.RequestIpUtil;

public class UserIdAndIPRateLimiterStrategy implements RateLimiterStrategy {

	@Override
	public String strategy() {
		HttpServletRequest servletRequest = getHttpServletRequest();
		String userId = servletRequest.getHeader(Constant.HEADER_USER_ID);
		return userId != null ? userId : RequestIpUtil.getIpAddress(servletRequest);
	}

}
