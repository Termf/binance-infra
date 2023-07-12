package com.binance.platform.resilience4j.timeLimiter.client;

import com.binance.platform.resilience4j.timeLimiter.BnTimeLimiter;
import io.github.resilience4j.core.lang.Nullable;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.configure.TimeLimiterAspectExt;
import io.github.resilience4j.timelimiter.configure.TimeLimiterConfigurationProperties;
import io.github.resilience4j.utils.AnnotationExtractor;
import io.github.resilience4j.utils.ValueResolver;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
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
import java.util.concurrent.*;
import java.util.function.Supplier;

import org.aspectj.lang.annotation.Aspect;

import com.binance.platform.resilience4j.BnBasicAspect;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.DEFAULT_TL_TIMEOUT;
import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.DEFAUL_TL_CANEL;


/**
 * author: sait xuc
 */
@Aspect
public class BnTimeLimiterAspect extends BnBasicAspect implements EmbeddedValueResolverAware, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(BnTimeLimiterAspect.class);

    private final BinanceTimeLimiterRegister btimeLimiterRegistry;

    private static final ScheduledExecutorService timeLimiterExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private StringValueResolver embeddedValueResolver;

    @Nullable
    @Autowired(required = false)
    private List<TimeLimiterAspectExt> timeLimiterAspectExtList;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TimeLimiterConfigurationProperties properties;

    public BnTimeLimiterAspect() {
        this.btimeLimiterRegistry = new BinanceTimeLimiterRegister();
        this.cleanup();
    }

    @Pointcut(
            value = "@within(bntimeLimiter) || @annotation(bntimeLimiter)",
            argNames = "bntimeLimiter"
    )
    public void matchAnnotatedClassOrMethod(BnTimeLimiter bntimeLimiter) {
    }

    @Around(
            value = "matchAnnotatedClassOrMethod(bntimeLimiterAnnotation)",
            argNames = "proceedingJoinPoint, bntimeLimiterAnnotation"
    )
    public Object bntimeLimiterAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, @Nullable BnTimeLimiter bntimeLimiterAnnotation) throws Throwable {

        Method method = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();
        if (bntimeLimiterAnnotation == null) {
            bntimeLimiterAnnotation = getTimeLimiterAnnotation(proceedingJoinPoint);
        }

        if (bntimeLimiterAnnotation == null) {
            return proceedingJoinPoint.proceed();
        } else {

            io.github.resilience4j.timelimiter.TimeLimiter timeLimiter = this.getOrCreateTimeLimiter(methodName, bntimeLimiterAnnotation);

            Class<?> returnType = method.getReturnType();
            String fallbackMethodValue = ValueResolver.resolve(this.embeddedValueResolver, bntimeLimiterAnnotation.fallbackMethod());

            if (StringUtils.isEmpty(fallbackMethodValue)) {
                return this.proceed(proceedingJoinPoint, methodName, timeLimiter, returnType);
            } else {
                Method fallbackMethod2 = getFallbackMethod(proceedingJoinPoint.getTarget(), fallbackMethodValue, method);
                try{
                    return this.proceed(proceedingJoinPoint, methodName, timeLimiter, returnType);
                }catch(Exception e) {
                    return fallbackMethod2.invoke(proceedingJoinPoint.getTarget(), proceedingJoinPoint.getArgs());
                }

            }
        }
    }

    private Object proceed(ProceedingJoinPoint proceedingJoinPoint, String methodName, io.github.resilience4j.timelimiter.TimeLimiter timeLimiter, Class<?> returnType) throws Throwable {
        if (this.timeLimiterAspectExtList != null && !this.timeLimiterAspectExtList.isEmpty()) {
            Iterator var5 = this.timeLimiterAspectExtList.iterator();

            while(var5.hasNext()) {
                TimeLimiterAspectExt timeLimiterAspectExt = (TimeLimiterAspectExt)var5.next();
                if (timeLimiterAspectExt.canHandleReturnType(returnType)) {
                    return timeLimiterAspectExt.handle(proceedingJoinPoint, timeLimiter, methodName);
                }
            }
        }
        if (!CompletionStage.class.isAssignableFrom(returnType)) {
            //throw new IllegalStateException("Not supported type by TimeLimiterAspect");
            return defaultHandle(proceedingJoinPoint, timeLimiter);
        } else {
            return handleJoinPointCompletableFuture(proceedingJoinPoint, timeLimiter);
        }
    }

    private static Object handleJoinPointCompletableFuture(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.timelimiter.TimeLimiter timeLimiter) throws Throwable {
        return timeLimiter.executeCompletionStage(timeLimiterExecutorService, () -> {
            try {
                return (CompletionStage)proceedingJoinPoint.proceed();
            } catch (Throwable var2) {
                throw new CompletionException(var2);
            }
        });
    }

    private static Object defaultHandle(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.timelimiter.TimeLimiter timeLimiter) throws Exception {
        Callable call = () -> {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable var2) {
                throw new CompletionException(var2);
            }
        };
        Supplier<Future<Object>> futureSupplier =  () -> timeLimiterExecutorService.submit(call);
        return timeLimiter.executeFutureSupplier(futureSupplier);

    }

    @Nullable
    private static BnTimeLimiter getTimeLimiterAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        if (proceedingJoinPoint.getTarget() instanceof Proxy) {
            logger.debug("The BnTimeLimiter annotation is kept on a interface which is acting as a proxy");
            return (BnTimeLimiter) AnnotationExtractor.extractAnnotationFromProxy(proceedingJoinPoint.getTarget(), BnTimeLimiter.class);
        } else {
            return (BnTimeLimiter)AnnotationExtractor.extract(proceedingJoinPoint.getTarget().getClass(), BnTimeLimiter.class);
        }
    }

    private io.github.resilience4j.timelimiter.TimeLimiter getOrCreateTimeLimiter(String methodName, BnTimeLimiter bntimeLimiterAnnotation) {
        TimeLimiterConfig pconfig = null;
        if(!isUseValue(this.applicationContext)) {
            pconfig = createTimeLimiterConfigByProp(bntimeLimiterAnnotation);
        }else{
            pconfig = createTimeLimiterConfig(bntimeLimiterAnnotation);
        }
        io.github.resilience4j.timelimiter.TimeLimiter timeLimiter = btimeLimiterRegistry.getOrCreate(methodName, pconfig);
        return timeLimiter;
    }

    private TimeLimiterConfig createTimeLimiterConfig(BnTimeLimiter bntimeLimiterAnnotation) {
        long tlTimeOut = DEFAULT_TL_TIMEOUT;
        boolean tlCancel = DEFAUL_TL_CANEL;

        if(bntimeLimiterAnnotation != null) {
            try{
                if(!org.apache.commons.lang.StringUtils.isEmpty(bntimeLimiterAnnotation.timeoutDuration())) {
                    tlTimeOut = Long.parseLong(bntimeLimiterAnnotation.timeoutDuration());
                }

                if(!org.apache.commons.lang.StringUtils.isEmpty(bntimeLimiterAnnotation.cancelRunningFuture())) {
                    tlCancel = Boolean.parseBoolean(bntimeLimiterAnnotation.cancelRunningFuture());
                }
            }catch(Exception e) {
                //use default;
            }
        }
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(tlTimeOut))
                .cancelRunningFuture(tlCancel)
                .build();
        return config;
    }

    private TimeLimiterConfig createTimeLimiterConfigByProp(BnTimeLimiter bntimeLimiterAnnotation) {
        long tlTimeOut = DEFAULT_TL_TIMEOUT;
        boolean tlCancel = DEFAUL_TL_CANEL;
        if(bntimeLimiterAnnotation != null) {
            try{
                String timeoutDurationKey = bntimeLimiterAnnotation.timeoutDuration();
                if(!org.apache.commons.lang.StringUtils.isEmpty(timeoutDurationKey)) {
                    //String timeoutDurationValue =  applicationContext.getEnvironment().getProperty(timeoutDurationKey);
                    String timeoutDurationValue =  this.getProperty(timeoutDurationKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(timeoutDurationValue)) {
                        tlTimeOut = Long.parseLong(timeoutDurationValue);
                    }
                }

                String cancelRunningFutureKey = bntimeLimiterAnnotation.timeoutDuration();
                if(!org.apache.commons.lang.StringUtils.isEmpty(cancelRunningFutureKey)) {
                    //String cancelRunningFutureValue = applicationContext.getEnvironment().getProperty(cancelRunningFutureKey);
                    String cancelRunningFutureValue = this.getProperty(cancelRunningFutureKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(cancelRunningFutureValue)) {
                        tlCancel = Boolean.parseBoolean(cancelRunningFutureValue);
                    }
                }
            }catch(Exception e) {
                //use default;
            }
        }
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(tlTimeOut))
                .cancelRunningFuture(tlCancel)
                .build();
        return config;
    }

    private void cleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timeLimiterExecutorService.shutdown();

            try {
                if (!timeLimiterExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                    timeLimiterExecutorService.shutdownNow();
                }
            } catch (InterruptedException var1) {
                if (!timeLimiterExecutorService.isTerminated()) {
                    timeLimiterExecutorService.shutdownNow();
                }

                Thread.currentThread().interrupt();
            }

        }));
    }

    public int getOrder() {
        return this.properties.getTimeLimiterAspectOrder();
    }

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }
}
