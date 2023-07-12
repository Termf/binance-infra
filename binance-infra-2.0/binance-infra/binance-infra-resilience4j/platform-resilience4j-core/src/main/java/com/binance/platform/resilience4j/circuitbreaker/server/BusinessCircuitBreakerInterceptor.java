package com.binance.platform.resilience4j.circuitbreaker.server;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.master.models.APIResponse.Type;
import com.binance.platform.common.MyJsonUtil;
import com.binance.platform.openfeign.body.CustomServletResponseWrapper;
import com.binance.platform.resilience4j.PlaceHolderResilience4j;
import com.binance.platform.resilience4j.circuitbreaker.ServerCircuitBreaker;
import com.binance.platform.resilience4j.ratelimiter.server.Resilience4jRateLimiter;
import com.google.common.collect.Maps;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class BusinessCircuitBreakerInterceptor extends PlaceHolderResilience4j implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resilience4jRateLimiter.class);

    private static final Map<String, CircuitBreaker> RESILIENCE4J_HANDLERMETHOD_CIRCUITBREAKER =
        Maps.newConcurrentMap();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        request.setAttribute("methodStartTime", System.nanoTime());
        if (handler instanceof HandlerMethod && response instanceof CustomServletResponseWrapper) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            String clazzAndMethodName = clazz.getName() + "_" + method.getName();
            ServerCircuitBreaker circuitBreakerAnno =
                AnnotationUtils.getAnnotation(handlerMethod.getMethod(), ServerCircuitBreaker.class);
            if (circuitBreakerAnno != null) {
                CircuitBreaker circuitBreaker = RESILIENCE4J_HANDLERMETHOD_CIRCUITBREAKER.get(clazzAndMethodName);
                if (circuitBreaker == null) {
                    this.buildCircuitBreaker(circuitBreakerAnno, clazzAndMethodName);
                    circuitBreaker = RESILIENCE4J_HANDLERMETHOD_CIRCUITBREAKER.get(clazzAndMethodName);
                }
                boolean acquirePermission = circuitBreaker.tryAcquirePermission();
                if (acquirePermission) {
                    return true;
                } else {
                    response.setHeader("Retry-After", "120");
                    response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    APIResponse<Object> apiResp = new APIResponse<Object>(APIResponse.Status.ERROR, Type.GENERAL,
                        GeneralCode.TOO_MANY_REQUESTS.getCode(), "has trigger ratlimiter", null);
                    try (PrintWriter out = response.getWriter()) {
                        out.print(MyJsonUtil.toJson(apiResp));
                    }
                    LOGGER.info("has trigger CircuitBreaker");
                    return false;
                }
            }
        }
        return true;
    }

    private void buildCircuitBreaker(ServerCircuitBreaker annotaion, String classNameMethodName) {
        int failureRateThreshold = getProperty(annotaion.failureRateThreshold());
        int waitDurationInOpenState = getProperty(annotaion.waitDurationInOpenState());
        int ringBufferSizeInHalfOpenState = getProperty(annotaion.ringBufferSizeInHalfOpenState());
        int ringBufferSizeInClosedState = getProperty(annotaion.ringBufferSizeInClosedState());
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()//
            .failureRateThreshold(failureRateThreshold)// 失败率阈值
            .waitDurationInOpenState(Duration.ofSeconds(waitDurationInOpenState))// 用来指定断路器从OPEN到HALF_OPEN状态等待的时长
            .permittedNumberOfCallsInHalfOpenState(ringBufferSizeInHalfOpenState)// 设置当断路器处于HALF_OPEN状态下的ringbuffer的大小，它存储了最近一段时间请求的成功失败状态
            .slidingWindow(ringBufferSizeInClosedState, ringBufferSizeInClosedState, SlidingWindowType.COUNT_BASED)// 设置当断路器处于CLOSED状态下的ringbuffer的大小，它存储了最近一段时间请求的成功失败状态
            .enableAutomaticTransitionFromOpenToHalfOpen()// 当waitDurationInOpenState时间一过，是否自动从OPEN切换到HALF_OPEN
            .ignoreExceptions(annotaion.ignoreExceptions()).build();
        CircuitBreaker circuitBreaker =
            CircuitBreakerRegistry.ofDefaults().circuitBreaker(classNameMethodName, circuitBreakerConfig);
        RESILIENCE4J_HANDLERMETHOD_CIRCUITBREAKER.put(classNameMethodName, circuitBreaker);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable Exception ex) throws Exception {
        if (handler instanceof HandlerMethod && response instanceof CustomServletResponseWrapper) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            String clazzAndMethodName = clazz.getName() + "_" + method.getName();
            Object methodStartTime = request.getAttribute("methodStartTime");
            long durationInNanos = System.nanoTime() - (Long)methodStartTime;
            CircuitBreaker circuitBreaker = RESILIENCE4J_HANDLERMETHOD_CIRCUITBREAKER.get(clazzAndMethodName);
            if (circuitBreaker != null) {
                if (response.getStatus() != HttpServletResponse.SC_OK) {
                    circuitBreaker.onError(durationInNanos, TimeUnit.NANOSECONDS,
                        new RuntimeException("Has trigger CiruitBreaker"));
                } else {
                    circuitBreaker.onSuccess(durationInNanos, TimeUnit.NANOSECONDS);
                }
            }
        }

    }

}
