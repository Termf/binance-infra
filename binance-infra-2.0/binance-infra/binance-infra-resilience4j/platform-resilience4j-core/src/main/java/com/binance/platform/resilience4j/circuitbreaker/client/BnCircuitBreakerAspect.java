package com.binance.platform.resilience4j.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerAspectExt;
import io.github.resilience4j.circuitbreaker.configure.CircuitBreakerConfigurationProperties;
import io.github.resilience4j.core.lang.Nullable;
import io.github.resilience4j.utils.AnnotationExtractor;
import io.github.resilience4j.utils.ValueResolver;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import com.binance.platform.resilience4j.circuitbreaker.BnCircuitBreaker;
import com.binance.platform.resilience4j.BnBasicAspect;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.*;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.DEFAULT_RB_HALFOPEN;

/**
 * author: sait xuc
 */
@Aspect
public class BnCircuitBreakerAspect extends BnBasicAspect implements EmbeddedValueResolverAware, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(BnCircuitBreakerAspect.class);

    @Autowired
    private CircuitBreakerConfigurationProperties circuitBreakerProperties;

    private final BinanceCircuitBreakerRegister bregister;

    @Autowired
    private ApplicationContext applicationContext;

    @Nullable
    @Autowired(required = false)
    private List<CircuitBreakerAspectExt> circuitBreakerAspectExtList;

    private StringValueResolver embeddedValueResolver;

    public BnCircuitBreakerAspect() {
        this.bregister = new BinanceCircuitBreakerRegister();
    }

    @Pointcut(value = "@within(bncircuitBreaker) || @annotation(bncircuitBreaker)", argNames = "bncircuitBreaker")
    public void matchAnnotatedClassOrMethod(BnCircuitBreaker bncircuitBreaker) {}

    @Around(value = "matchAnnotatedClassOrMethod(bncircuitBreakerAnnotation)",
        argNames = "proceedingJoinPoint, bncircuitBreakerAnnotation")
    public Object circuitBreakerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint,
        @Nullable BnCircuitBreaker bncircuitBreakerAnnotation) throws Throwable {
        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (bncircuitBreakerAnnotation == null) {
            bncircuitBreakerAnnotation = this.getCircuitBreakerAnnotation(proceedingJoinPoint);
        }

        if (bncircuitBreakerAnnotation == null) {
            return proceedingJoinPoint.proceed();
        } else {
            // String backend = bncircuitBreakerAnnotation.name();
            io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker =
                this.getOrCreateCircuitBreaker(methodName, bncircuitBreakerAnnotation);
            Class<?> returnType = method.getReturnType();
            String fallbackMethodValue =
                ValueResolver.resolve(this.embeddedValueResolver, bncircuitBreakerAnnotation.fallbackMethod());
            if (StringUtils.isEmpty(fallbackMethodValue)) {
                return this.proceed(proceedingJoinPoint, methodName, circuitBreaker, returnType);
            } else {

                Method fallbackMethod2 =
                    getFallbackMethod(proceedingJoinPoint.getTarget(), fallbackMethodValue, method);

                try {
                    return this.proceed(proceedingJoinPoint, methodName, circuitBreaker, returnType);
                } catch (Exception e) {
                    return fallbackMethod2.invoke(proceedingJoinPoint.getTarget(), proceedingJoinPoint.getArgs());
                }
            }
        }
    }

    @Nullable
    private BnCircuitBreaker getCircuitBreakerAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (logger.isDebugEnabled()) {
            logger.debug("circuitBreaker parameter is null");
        }

        if (proceedingJoinPoint.getTarget() instanceof Proxy) {
            logger.debug("The bncircuit breaker annotation is kept on a interface which is acting as a proxy");
            return (BnCircuitBreaker)AnnotationExtractor.extractAnnotationFromProxy(proceedingJoinPoint.getTarget(),
                BnCircuitBreaker.class);
        } else {
            return (BnCircuitBreaker)AnnotationExtractor.extract(proceedingJoinPoint.getTarget().getClass(),
                BnCircuitBreaker.class);
        }
    }

    private io.github.resilience4j.circuitbreaker.CircuitBreaker getOrCreateCircuitBreaker(String methodName,
        BnCircuitBreaker bncircuitBreakerAnnotation) {
        CircuitBreakerConfig config = null;

        if (!isUseValue(this.applicationContext)) {
            config = createCircuitBreakerConfigByProp(bncircuitBreakerAnnotation);
        } else {
            config = createCircuitBreakerConfig(bncircuitBreakerAnnotation);
        }
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker =
            this.bregister.getOrCreate(methodName, config);
        return circuitBreaker;
    }

    private Object proceed(ProceedingJoinPoint proceedingJoinPoint, String methodName,
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker, Class<?> returnType) throws Throwable {
        if (this.circuitBreakerAspectExtList != null && !this.circuitBreakerAspectExtList.isEmpty()) {
            Iterator var5 = this.circuitBreakerAspectExtList.iterator();

            while (var5.hasNext()) {
                CircuitBreakerAspectExt circuitBreakerAspectExt = (CircuitBreakerAspectExt)var5.next();
                if (circuitBreakerAspectExt.canHandleReturnType(returnType)) {
                    return circuitBreakerAspectExt.handle(proceedingJoinPoint, circuitBreaker, methodName);
                }
            }
        }

        return CompletionStage.class.isAssignableFrom(returnType)
            ? this.handleJoinPointCompletableFuture(proceedingJoinPoint, circuitBreaker)
            : this.defaultHandling(proceedingJoinPoint, circuitBreaker);
    }

    private Object handleJoinPointCompletableFuture(ProceedingJoinPoint proceedingJoinPoint,
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker) {
        return circuitBreaker.executeCompletionStage(() -> {
            try {
                return (CompletionStage)proceedingJoinPoint.proceed();
            } catch (Throwable var2) {
                throw new CompletionException(var2);
            }
        });
    }

    private Object defaultHandling(ProceedingJoinPoint proceedingJoinPoint,
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker) throws Throwable {
        proceedingJoinPoint.getClass();
        return circuitBreaker.executeCheckedSupplier(proceedingJoinPoint::proceed);
    }

    public int getOrder() {
        return this.circuitBreakerProperties.getCircuitBreakerAspectOrder();
    }

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    private CircuitBreakerConfig createCircuitBreakerConfig(BnCircuitBreaker annontion) {
        int failRate = DEFAULT_FAIL_RATE;
        long waitDuration = DEFAULT_WAIT_DURATION;
        int ringbuffrByClosed = DEFAULT_RB_CLOSED;
        int ringbufferByHopen = DEFAULT_RB_HALFOPEN;
        if (annontion != null) {
            try {
                if (!org.apache.commons.lang.StringUtils.isEmpty(annontion.failureRateThreshold())) {
                    failRate = Integer.parseInt(annontion.failureRateThreshold());
                }

                if (!org.apache.commons.lang.StringUtils.isEmpty(annontion.waitDurationInOpenState())) {
                    waitDuration = Long.parseLong(annontion.waitDurationInOpenState());
                }

                if (!org.apache.commons.lang.StringUtils.isEmpty(annontion.ringBufferSizeInClosedState())) {
                    ringbuffrByClosed = Integer.parseInt(annontion.ringBufferSizeInClosedState());
                }

                if (!org.apache.commons.lang.StringUtils.isEmpty(annontion.ringBufferSizeInHalfOpenState())) {
                    ringbufferByHopen = Integer.parseInt(annontion.ringBufferSizeInHalfOpenState());
                }

            } catch (Exception e) {
                // use default;
            }
        }
        CircuitBreakerConfig circuitBreakerConfig = new CircuitBreakerConfig.Builder().failureRateThreshold(failRate)// 熔断器关闭状态和半开状态使用的同一个失败率阈值
            // .slidingWindow(5,2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .ringBufferSizeInClosedState(ringbuffrByClosed).ringBufferSizeInHalfOpenState(ringbufferByHopen)
            .waitDurationInOpenState(Duration.ofSeconds(waitDuration)).ignoreExceptions(annontion.ignoreExceptions())
            .enableAutomaticTransitionFromOpenToHalfOpen()// 如果置为true，当等待时间结束会自动由打开变为半开，若置为false，则需要一个请求进入来触发熔断器状态转换
            .build();

        return circuitBreakerConfig;
    }

    private CircuitBreakerConfig createCircuitBreakerConfigByProp(BnCircuitBreaker annontion) {
        int failRate = DEFAULT_FAIL_RATE;
        long waitDuration = DEFAULT_WAIT_DURATION;
        int ringbuffrByClosed = DEFAULT_RB_CLOSED;
        int ringbufferByHopen = DEFAULT_RB_HALFOPEN;
        if (annontion != null) {
            try {
                String failureRateThresholdKey = annontion.failureRateThreshold();
                if (!org.apache.commons.lang.StringUtils.isEmpty(failureRateThresholdKey)) {
                    // String failureRateThresholdKeyValue =
                    // applicationContext.getEnvironment().getProperty(failureRateThresholdKey);
                    String failureRateThresholdKeyValue = this.getProperty(failureRateThresholdKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(failureRateThresholdKeyValue)) {
                        failRate = Integer.parseInt(failureRateThresholdKeyValue);
                    }
                }

                String waitDurationInOpenStateKey = annontion.waitDurationInOpenState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(waitDurationInOpenStateKey)) {
                    // String waitDurationInOpenStateValue =
                    // applicationContext.getEnvironment().getProperty(waitDurationInOpenStateKey);
                    String waitDurationInOpenStateValue =
                        this.getProperty(waitDurationInOpenStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(waitDurationInOpenStateValue)) {
                        waitDuration = Long.parseLong(waitDurationInOpenStateValue);
                    }
                }

                String ringBufferSizeInHalfOpenStateKey = annontion.ringBufferSizeInHalfOpenState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInHalfOpenStateKey)) {
                    // String ringBufferSizeInHalfOpenStateValue =
                    // applicationContext.getEnvironment().getProperty(ringBufferSizeInHalfOpenStateKey);
                    String ringBufferSizeInHalfOpenStateValue =
                        this.getProperty(ringBufferSizeInHalfOpenStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInHalfOpenStateValue)) {
                        ringbufferByHopen = Integer.parseInt(ringBufferSizeInHalfOpenStateValue);
                    }
                }

                String ringBufferSizeInClosedStateKey = annontion.ringBufferSizeInClosedState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInClosedStateKey)) {
                    // String ringBufferSizeInClosedStateValue =
                    // applicationContext.getEnvironment().getProperty(ringBufferSizeInClosedStateKey);
                    String ringBufferSizeInClosedStateValue =
                        this.getProperty(ringBufferSizeInClosedStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInClosedStateValue)) {
                        ringbuffrByClosed = Integer.parseInt(ringBufferSizeInClosedStateValue);
                    }
                }

            } catch (Exception e) {
                // use default;
            }
        }
        CircuitBreakerConfig circuitBreakerConfig = new CircuitBreakerConfig.Builder().failureRateThreshold(failRate)// 熔断器关闭状态和半开状态使用的同一个失败率阈值
            // .slidingWindow(5,2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .ringBufferSizeInClosedState(ringbuffrByClosed).ringBufferSizeInHalfOpenState(ringbufferByHopen)
            .waitDurationInOpenState(Duration.ofSeconds(waitDuration)).ignoreExceptions(annontion.ignoreExceptions())
            .enableAutomaticTransitionFromOpenToHalfOpen()// 如果置为true，当等待时间结束会自动由打开变为半开，若置为false，则需要一个请求进入来触发熔断器状态转换
            .build();

        return circuitBreakerConfig;

    }

}
