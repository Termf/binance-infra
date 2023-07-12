package com.binance.platform.openfeign.body;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.binance.platform.common.RpcContext;

public class GrayCookieAndHeaderFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(GrayCookieAndHeaderFilter.class);

	private static final String GRAY_ENV_HEADER = "X-GRAY-ENV";

	private static final String COOKIE_KEY = "gray";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		Cookie cookie = WebUtils.getCookie(request, COOKIE_KEY);
		if (cookie != null) {
			String grayValue = cookie.getValue();
			if (request instanceof CustomeHeaderServletRequestWrapper) {
				logger.info("this is for admin mgs! not for gateway! Have receive gray request,and the gray value is:"
						+ grayValue);
				CustomeHeaderServletRequestWrapper requestWrapper = (CustomeHeaderServletRequestWrapper) request;
				requestWrapper.putHeader(GRAY_ENV_HEADER, grayValue);
				chain.doFilter(requestWrapper, response);
			}
			// 这段逻辑有点恶心，CustomServletRequestWrapper按照道理是可以对任何HttpServletRequest进行适配的，但是历史问题不敢修改，不动以前老代码
			else {
				RpcContext.getContext().set(GRAY_ENV_HEADER, grayValue);
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}

	}

}
