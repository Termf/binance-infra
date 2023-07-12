package com.binance.platform.monitor.logging.tracing;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.takin.TakinIsNotReadyException;
import com.binance.platform.monitor.takin.TestContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import com.binance.platform.common.RpcContext;
import com.binance.platform.common.TrackingUtils;

public class TracingOncePerRequestFilter extends OncePerRequestFilter {

    public static final String HEADER_ENCODED_HEADER_NAME = "x-header-encoded";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = TrackingUtils.getTraceIdFromRequest(request);
            logger.debug(String.format("receive traceId from upstream services and the traceId is: %s", traceId));
            traceId = decodeHeaderValue(request, traceId);
            TrackingUtils.saveTrace(traceId);
            //增加一个关于压测的判断。 如果是压测流量，但是takin agent 没有就绪，那么抛异常
            if (TestContext.isTestRequest(request) && !TestContext.isTakinReady()) {
                throw new TakinIsNotReadyException();
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            RpcContext.clear();
        }
    }

    /**
     * 这个逻辑需要与如下方法保持一致
     * com.binance.platform.openfeign.body.CustomeHeaderServletRequestWrapper#isHeaderEncoded()
     *
     * @return
     */
    private boolean isHeaderEncoded(HttpServletRequest request) {
        String value = request.getHeader(HEADER_ENCODED_HEADER_NAME);
        return StringUtils.isNotBlank(value) && BooleanUtils.toBoolean(value);
    }

    private String decodeHeaderValue(HttpServletRequest request, String value) {
        try {
            // 如果value被encode过，才进行decode
            String swithEncodeDecode = EnvUtil.getProperty("binance.header.encodedecode.enabled", "false");
            if (isHeaderEncoded(request) && BooleanUtils.toBoolean(swithEncodeDecode)) {
                return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
            }
        } catch (Throwable e) {
            return value;
        }
        return value;
    }

}
