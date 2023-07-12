package com.binance.platform.openfeign.compress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.binance.platform.env.EnvUtil;
import com.binance.platform.openfeign.body.CustomServletResponseWrapper;

@Deprecated
public class GzipHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            if (handlerMethod.getMethod().getAnnotationsByType(Gzip.class).length > 0) {
                if (response instanceof CustomServletResponseWrapper) {
                    CustomServletResponseWrapper customServletResponseWrapper = (CustomServletResponseWrapper)response;
                    String openCompress = EnvUtil.getProperty("com.binance.intra.compress.switch", "false");
                    if (BooleanUtils.toBoolean(openCompress)) {
                        customServletResponseWrapper.setHeader("X-RESPONSE-UNZIP", "true");
                        customServletResponseWrapper.setIsGzip(true);
                    }
                }

            }
        }

        return true;
    }
}
