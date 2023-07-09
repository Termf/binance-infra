package com.binance.infra.sample.resilience4j.server.custom;

import com.binance.master.utils.StringUtils;
import com.binance.platform.resilience4j.ratelimiter.RateLimiterStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lucas.w
 * @version 1.0.0
 * @since 2021-08-12
 */
@Component
public class ChannelStrategy implements RateLimiterStrategy {

    @Override
    public String strategy() {
        HttpServletRequest servletRequest = getHttpServletRequest();
        String origin = servletRequest.getHeader("x-origin");
        return StringUtils.isEmpty(origin) ? "default" : origin;
    }
}
