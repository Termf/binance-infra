package com.binance.platform.resilience4j.ratelimiter.client;

import io.github.resilience4j.core.lang.Nullable;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.configure.RateLimiterAspectExt;
import io.github.resilience4j.ratelimiter.configure.RateLimiterConfigurationProperties;
import io.github.resilience4j.utils.AnnotationExtractor;
import io.github.resilience4j.utils.ValueResolver;
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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;


import com.binance.platform.resilience4j.ratelimiter.BnRateLimiter;
import com.binance.platform.resilience4j.BnBasicAspect;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.DEFAULT_LIMIT_FORPERIOD;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.DEFAULT_LIMIT_REFRESHPERIOD;

/**
 * author: sait xuc
 */
@Aspect
public class BnRateLimiterAspect extends BnBasicAspect implements EmbeddedValueResolverAware, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(BnRateLimiterAspect.class);

    private static final String RATE_LIMITER_RECEIVED = "Created or bnretrieved rate limiter '{}' with period: '{}'; limit for period: '{}'; timeout: '{}'; method: '{}'";

    private final BinanceRateLimiterRegister bregister;

    @Autowired
    private RateLimiterConfigurationProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Nullable
    @Autowired(required = false)
    private List<RateLimiterAspectExt> rateLimiterAspectExtList;

    private StringValueResolver embeddedValueResolver;

    public BnRateLimiterAspect() {
        this.bregister = new BinanceRateLimiterRegister();
    }

    @Pointcut(
            value = "@within(bnrateLimiter) || @annotation(bnrateLimiter)",
            argNames = "bnrateLimiter"
    )
    public void matchAnnotatedClassOrMethod(BnRateLimiter bnrateLimiter) {
    }

    @Around(
            value = "matchAnnotatedClassOrMethod(bnrateLimiterAnnotation)",
            argNames = "proceedingJoinPoint, bnrateLimiterAnnotation"
    )
    public Object rateLimiterAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, @Nullable BnRateLimiter bnrateLimiterAnnotation) throws Throwable {
        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (bnrateLimiterAnnotation == null) {
            bnrateLimiterAnnotation = this.getRateLimiterAnnotation(proceedingJoinPoint);
        }
        if (bnrateLimiterAnnotation == null) {
            return proceedingJoinPoint.proceed();
        } else {
            //String name = rateLimiterAnnotation.name();
            io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = this.getOrCreateRateLimiter(methodName, bnrateLimiterAnnotation);
            Class<?> returnType = method.getReturnType();
            String fallbackMethodValue = ValueResolver.resolve(this.embeddedValueResolver, bnrateLimiterAnnotation.fallbackMethod());

            if (StringUtils.isEmpty(fallbackMethodValue)) {
                return this.proceed(proceedingJoinPoint, methodName, returnType, rateLimiter);
            } else {

                Method fallbackMethod2 = getFallbackMethod(proceedingJoinPoint.getTarget(), fallbackMethodValue, method);

                try{
                    return this.proceed(proceedingJoinPoint, methodName, returnType, rateLimiter);
                }catch(Exception e) {
                    return fallbackMethod2.invoke(proceedingJoinPoint.getTarget(), proceedingJoinPoint.getArgs());
                }
            }
        }
    }


    private Object proceed(ProceedingJoinPoint proceedingJoinPoint, String methodName, Class<?> returnType, io.github.resilience4j.ratelimiter.RateLimiter rateLimiter) throws Throwable {
        if (this.rateLimiterAspectExtList != null && !this.rateLimiterAspectExtList.isEmpty()) {
            Iterator var5 = this.rateLimiterAspectExtList.iterator();

            while(var5.hasNext()) {
                RateLimiterAspectExt rateLimiterAspectExt = (RateLimiterAspectExt)var5.next();
                if (rateLimiterAspectExt.canHandleReturnType(returnType)) {
                    return rateLimiterAspectExt.handle(proceedingJoinPoint, rateLimiter, methodName);
                }
            }
        }

        return CompletionStage.class.isAssignableFrom(returnType) ? this.handleJoinPointCompletableFuture(proceedingJoinPoint, rateLimiter) : this.handleJoinPoint(proceedingJoinPoint, rateLimiter);
    }

    private io.github.resilience4j.ratelimiter.RateLimiter getOrCreateRateLimiter(String methodName, BnRateLimiter annotion) {
        RateLimiterConfig config = null;
        if(!isUseValue(this.applicationContext)) {
            config = createRateLimiterConfigByProp(annotion);
        }else{
            config = createRateLimiterConfig(annotion);
        }

        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = this.bregister.getOrCreate(methodName, config);
        return rateLimiter;
    }

    @Nullable
    private BnRateLimiter getRateLimiterAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (proceedingJoinPoint.getTarget() instanceof Proxy) {
            logger.debug("The rate limiter annotation is kept on a interface which is acting as a proxy");
            return (BnRateLimiter) AnnotationExtractor.extractAnnotationFromProxy(proceedingJoinPoint.getTarget(), BnRateLimiter.class);
        } else {
            return (BnRateLimiter)AnnotationExtractor.extract(proceedingJoinPoint.getTarget().getClass(), BnRateLimiter.class);
        }
    }

    private Object handleJoinPoint(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.ratelimiter.RateLimiter rateLimiter) throws Throwable {
        proceedingJoinPoint.getClass();
        return rateLimiter.executeCheckedSupplier(proceedingJoinPoint::proceed);
    }

    private Object handleJoinPointCompletableFuture(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.ratelimiter.RateLimiter rateLimiter) {
        return rateLimiter.executeCompletionStage(() -> {
            try {
                return (CompletionStage)proceedingJoinPoint.proceed();
            } catch (Throwable var2) {
                throw new CompletionException(var2);
            }
        });
    }

    public int getOrder() {
        return this.properties.getRateLimiterAspectOrder();
    }

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    private RateLimiterConfig createRateLimiterConfig(BnRateLimiter annotion) {
        int limitForPeriod = DEFAULT_LIMIT_FORPERIOD;
        long timeoutDuration = 1L;
        long limitRefreshPeriod = DEFAULT_LIMIT_REFRESHPERIOD;
        if(annotion != null) {
            try{
                if(!org.apache.commons.lang.StringUtils.isEmpty(annotion.limitForPeriod())) {
                    limitForPeriod = Integer.parseInt(annotion.limitForPeriod());
                }
                if(!org.apache.commons.lang.StringUtils.isEmpty(annotion.limitRefreshPeriod())) {
                    limitRefreshPeriod = Long.parseLong(annotion.limitRefreshPeriod());
                }
            }catch(Exception e) {
                //use default;
            }
        }
        RateLimiterConfig rateLimiterConfig = new RateLimiterConfig.Builder()
                .limitForPeriod(limitForPeriod)
                .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                .limitRefreshPeriod(Duration.ofNanos(limitRefreshPeriod))
                .build();
        return rateLimiterConfig;
    }


    private RateLimiterConfig createRateLimiterConfigByProp(BnRateLimiter annotion) {
        int limitForPeriod = DEFAULT_LIMIT_FORPERIOD;
        long timeoutDuration = 1L;
        long limitRefreshPeriod = DEFAULT_LIMIT_REFRESHPERIOD;

        if(annotion != null) {
            try{
                String limitForPeriodKey = annotion.limitForPeriod();
                if(!org.apache.commons.lang.StringUtils.isEmpty(limitForPeriodKey)) {
                    //String limitForPeriodValue =  applicationContext.getEnvironment().getProperty(limitForPeriodKey);
                    String limitForPeriodValue =  this.getProperty(limitForPeriodKey, applicationContext);

                    if(!org.apache.commons.lang.StringUtils.isEmpty(limitForPeriodValue)) {
                        limitForPeriod = Integer.parseInt(limitForPeriodValue);
                    }
                }

                String limitRefreshPeriodKey = annotion.limitRefreshPeriod();
                if(!org.apache.commons.lang.StringUtils.isEmpty(limitRefreshPeriodKey)) {
                    //String limitRefreshPeriodValue = applicationContext.getEnvironment().getProperty(limitRefreshPeriodKey);
                    String limitRefreshPeriodValue = this.getProperty(limitRefreshPeriodKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(limitRefreshPeriodValue)) {
                        limitRefreshPeriod = Long.parseLong(limitRefreshPeriodValue);
                    }
                }

            }catch(Exception e) {
                //use default;
            }
        }
        RateLimiterConfig rateLimiterConfig = new RateLimiterConfig.Builder()
                .limitForPeriod(limitForPeriod)
                .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                .limitRefreshPeriod(Duration.ofNanos(limitRefreshPeriod))
                .build();
        return rateLimiterConfig;


    }


}