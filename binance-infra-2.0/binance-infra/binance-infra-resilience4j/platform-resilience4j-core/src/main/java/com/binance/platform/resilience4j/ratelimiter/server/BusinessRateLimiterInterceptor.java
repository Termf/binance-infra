package com.binance.platform.resilience4j.ratelimiter.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.binance.platform.resilience4j.ratelimiter.ServerRateLimiter;

public class BusinessRateLimiterInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;

    public BusinessRateLimiterInterceptor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            ServerRateLimiter methodLimit =
                AnnotationUtils.getAnnotation(handlerMethod.getMethod(), ServerRateLimiter.class);
            ServerRateLimiter classlimit =
                AnnotationUtils.getAnnotation(handlerMethod.getMethod().getDeclaringClass(), ServerRateLimiter.class);
            if (classlimit != null && methodLimit == null) {
                boolean useRedis = classlimit.useRedis();
                if (!useRedis) {
                    Resilience4jRateLimiter rateLimiter = Resilience4jRateLimiter.getInstance();
                    rateLimiter.setApplicationContext(applicationContext);
                    return rateLimiter.preHandle(request, response, handlerMethod);
                } else {
                    RedisRateLimiter rateLimiter = RedisRateLimiter.getInstance();
                    rateLimiter.setApplicationContext(applicationContext);
                    return rateLimiter.preHandle(request, response, handlerMethod);
                }
            }
            if (methodLimit != null) {
                boolean useRedis = methodLimit.useRedis();
                if (!useRedis) {
                    Resilience4jRateLimiter rateLimiter = Resilience4jRateLimiter.getInstance();
                    rateLimiter.setApplicationContext(applicationContext);
                    return rateLimiter.preHandle(request, response, handlerMethod);
                } else {
                    RedisRateLimiter rateLimiter = RedisRateLimiter.getInstance();
                    rateLimiter.setApplicationContext(applicationContext);
                    return rateLimiter.preHandle(request, response, handlerMethod);
                }
            }

        }
        return true;
    }

}
