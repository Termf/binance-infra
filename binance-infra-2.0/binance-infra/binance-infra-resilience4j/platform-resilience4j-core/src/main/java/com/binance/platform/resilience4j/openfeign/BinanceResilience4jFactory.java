package com.binance.platform.resilience4j.openfeign;

import com.binance.platform.resilience4j.bulkhead.BinanceBulkheadRegister;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cloud.openfeign.FeignClient;
import feign.Target;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.openfeign.FeignContext;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;


import com.binance.platform.resilience4j.circuitbreaker.ClientCircuitBreaker;
import com.binance.platform.resilience4j.ratelimiter.ClientRateLimiter;
import com.binance.platform.resilience4j.timeLimiter.ClientTimeLimiter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import static com.binance.platform.resilience4j.openfeign.ClientResilience4jConstants.*;



/**
 * author: sait xuc
 */
public interface BinanceResilience4jFactory {

    FeignDecorator create(Target<?> target, Method method, ApplicationContext applicationContext, FeignContext feignContext, ScheduledExecutorService timeLimiterExecutorService);

    final class Default implements BinanceResilience4jFactory {

        private static final Pattern PATTERN = Pattern.compile("\\Q${\\E(.+?)\\Q}\\E");

        private static final Map<String, Pair<String, String>> PROPERTY_KEYS = Maps.newHashMap();

        @Override
        public FeignDecorator create(Target<?> target,
                                     Method method,
                                     ApplicationContext applicationContext,
                                     FeignContext feignContext,
                                     ScheduledExecutorService timeLimiterExecutorService) {
            String name = target.type().getName();
            //TODO 从MyHystrixAnno 中获取配置多种参数
            ClientCircuitBreaker annontion = AnnotationUtils.getAnnotation(method, ClientCircuitBreaker.class);
            if(annontion == null) {
                annontion = AnnotationUtils.getAnnotation(method.getDeclaringClass(), ClientCircuitBreaker.class);
            }
            CircuitBreaker circuitBreaker = null;
            if(annontion != null) {
                circuitBreaker = this.createCircuitBreaker(annontion, name, applicationContext);
            }

            ClientTimeLimiter tlnnotion = AnnotationUtils.getAnnotation(method, ClientTimeLimiter.class);
            if(tlnnotion == null) {
                tlnnotion = AnnotationUtils.getAnnotation(method.getDeclaringClass(), ClientTimeLimiter.class);
            }
            TimeLimiter timeLimiter = null;
            if(tlnnotion != null) {
                timeLimiter = this.createTimeLimiter(tlnnotion, name, applicationContext);
            }

            ClientRateLimiter slAnnotion = AnnotationUtils.getAnnotation(method, ClientRateLimiter.class);
            if(slAnnotion == null) {
                slAnnotion = AnnotationUtils.getAnnotation(method.getDeclaringClass(), ClientRateLimiter.class);
            }
            RateLimiter rateLimiter = null;
            if(slAnnotion != null) {
                rateLimiter = this.createRateLimiter(slAnnotion, name, applicationContext);
            }
            FeignDecorators.Builder builder = FeignDecorators.builder();
            if(timeLimiter != null) {
                builder.withTimeLimit(timeLimiter, timeLimiterExecutorService);
            }
            if(rateLimiter != null) {
                builder.withRateLimiter(rateLimiter);
            }
            if(circuitBreaker != null) {
                builder.withCircuitBreaker(circuitBreaker);
            }
            Class<?> fallbackClass = this.getFallbackClass(annontion);
            if(fallbackClass == null
                    || fallbackClass == void.class) {
                //获取fallback
                FeignClient client = target.type().getAnnotation(FeignClient.class);
                if(feignContext.getInstance(name,client.fallback()) != null) {
                    builder.withFallback(feignContext.getInstance(name,client.fallback()), CallNotPermittedException.class);
                }else{
                    if(annontion != null) {
                        throw new RuntimeException(" fallback class must bet set.  ");
                    }
                }
            }else{
                Object instance = applicationContext.getBean(fallbackClass);
                if(instance != null) {
                    builder.withFallback(instance, CallNotPermittedException.class);
                }
            }
            BinanceBulkheadRegister binanceBulkheadRegister = applicationContext.getBean(BinanceBulkheadRegister.class);
            // add bulkhead
            binanceBulkheadRegister.withBulkhead(builder, target.name(), applicationContext);
            return builder.build();
        }




        private CircuitBreaker createCircuitBreaker(ClientCircuitBreaker annontion, String name, ApplicationContext applicationContext) {
            CircuitBreaker circuitBreaker = null;
            if(annontion != null) {
                CircuitBreakerConfig circuitBreakerConfig = null;
                if(!isUseValue(applicationContext)) {
                    circuitBreakerConfig = createCircuitBreakerConfigByProp(annontion, applicationContext);
                }else {
                    circuitBreakerConfig = createCircuitBreakerConfig(annontion);
                }
                circuitBreaker = CircuitBreaker.of(name, circuitBreakerConfig);
            }
            return circuitBreaker;
        }


        private TimeLimiter createTimeLimiter(ClientTimeLimiter annotion, String name, ApplicationContext applicationContext) {
            TimeLimiter timeLimiter = null;
            if(annotion != null) {
                TimeLimiterConfig config = null;
                if(!isUseValue(applicationContext)) {
                    config =createTimeLimiterConfigbyProp(annotion, applicationContext);
                }else {
                    config = createTimeLimiterConfig(annotion);
                }
                timeLimiter = TimeLimiter.of(name, config);
            }
            return timeLimiter;
        }

        private RateLimiter createRateLimiter(ClientRateLimiter annotion, String name, ApplicationContext applicationContext) {
            RateLimiter rateLimiter = null;
            if(annotion != null) {
                RateLimiterConfig rateLimiterConfig = null;
                if(!isUseValue(applicationContext)) {
                    rateLimiterConfig = createRateLimiterConfigByProp(annotion, applicationContext);
                }else{
                    rateLimiterConfig = createRateLimiterConfig(annotion);
                }
                rateLimiter = RateLimiter.of(name, rateLimiterConfig);
            }
            return rateLimiter;
        }

        private Class<?> getFallbackClass(ClientCircuitBreaker annontion) {
           if(annontion == null) {
               return void.class;
           }
            Class<?> fclass = annontion.fallback();
            if(fclass == null) {
                fclass = void.class;
            }
            return fclass;
        }

        private CircuitBreakerConfig createCircuitBreakerConfig(ClientCircuitBreaker annontion) {
            int failRate = DEFAULT_FAIL_RATE;
            long waitDuration = DEFAULT_WAIT_DURATION;
            int ringbuffrByClosed = DEFAULT_RB_CLOSED;
            int ringbufferByHopen = DEFAULT_RB_HALFOPEN;
            try{
                if(!StringUtils.isEmpty(annontion.failureRateThreshold())) {
                    failRate = Integer.parseInt(annontion.failureRateThreshold());
                }

                if(!StringUtils.isEmpty(annontion.waitDurationInOpenState())) {
                    waitDuration = Long.parseLong(annontion.waitDurationInOpenState());
                }

                if(!StringUtils.isEmpty(annontion.ringBufferSizeInClosedState())) {
                    ringbuffrByClosed = Integer.parseInt(annontion.ringBufferSizeInClosedState());
                }

                if(!StringUtils.isEmpty(annontion.ringBufferSizeInHalfOpenState())) {
                    ringbufferByHopen = Integer.parseInt(annontion.ringBufferSizeInHalfOpenState());
                }
            }catch(Exception e) {
                //use default;
            }
            CircuitBreakerConfig circuitBreakerConfig = new CircuitBreakerConfig.Builder()
                    .failureRateThreshold(failRate)//熔断器关闭状态和半开状态使用的同一个失败率阈值
                    //.slidingWindow(5,2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .ringBufferSizeInClosedState(ringbuffrByClosed)
                    .ringBufferSizeInHalfOpenState(ringbufferByHopen)
                    .waitDurationInOpenState(Duration.ofSeconds(waitDuration))
                    .ignoreExceptions(annontion.ignoreExceptions())
                    .enableAutomaticTransitionFromOpenToHalfOpen()//如果置为true，当等待时间结束会自动由打开变为半开，若置为false，则需要一个请求进入来触发熔断器状态转换
                    .build();
            return circuitBreakerConfig;
        }

        private CircuitBreakerConfig createCircuitBreakerConfigByProp(ClientCircuitBreaker annontion, ApplicationContext applicationContext) {
            int failRate = DEFAULT_FAIL_RATE;
            long waitDuration = DEFAULT_WAIT_DURATION;
            int ringbuffrByClosed = DEFAULT_RB_CLOSED;
            int ringbufferByHopen = DEFAULT_RB_HALFOPEN;

            try {
                String failureRateThresholdKey = annontion.failureRateThreshold();
                if (!org.apache.commons.lang.StringUtils.isEmpty(failureRateThresholdKey)) {
                    //String failureRateThresholdKeyValue =  applicationContext.getEnvironment().getProperty(failureRateThresholdKey);
                    String failureRateThresholdKeyValue = this.getProperty(failureRateThresholdKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(failureRateThresholdKeyValue)) {
                        failRate = Integer.parseInt(failureRateThresholdKeyValue);
                    }
                }

                String waitDurationInOpenStateKey = annontion.waitDurationInOpenState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(waitDurationInOpenStateKey)) {
                    //String waitDurationInOpenStateValue = applicationContext.getEnvironment().getProperty(waitDurationInOpenStateKey);
                    String waitDurationInOpenStateValue = this.getProperty(waitDurationInOpenStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(waitDurationInOpenStateValue)) {
                        waitDuration = Long.parseLong(waitDurationInOpenStateValue);
                    }
                }

                String ringBufferSizeInHalfOpenStateKey = annontion.ringBufferSizeInHalfOpenState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInHalfOpenStateKey)) {
                    //String ringBufferSizeInHalfOpenStateValue = applicationContext.getEnvironment().getProperty(ringBufferSizeInHalfOpenStateKey);
                    String ringBufferSizeInHalfOpenStateValue = this.getProperty(ringBufferSizeInHalfOpenStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInHalfOpenStateValue)) {
                        ringbufferByHopen = Integer.parseInt(ringBufferSizeInHalfOpenStateValue);
                    }
                }

                String ringBufferSizeInClosedStateKey = annontion.ringBufferSizeInClosedState();
                if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInClosedStateKey)) {
                    //String ringBufferSizeInClosedStateValue = applicationContext.getEnvironment().getProperty(ringBufferSizeInClosedStateKey);
                    String ringBufferSizeInClosedStateValue = this.getProperty(ringBufferSizeInClosedStateKey, applicationContext);
                    if (!org.apache.commons.lang.StringUtils.isEmpty(ringBufferSizeInClosedStateValue)) {
                        ringbuffrByClosed = Integer.parseInt(ringBufferSizeInClosedStateValue);
                    }
                }

            } catch (Exception e) {
                //use default;
            }
            CircuitBreakerConfig circuitBreakerConfig = new CircuitBreakerConfig.Builder()
                    .failureRateThreshold(failRate)//熔断器关闭状态和半开状态使用的同一个失败率阈值
                    //.slidingWindow(5,2, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                    .ringBufferSizeInClosedState(ringbuffrByClosed)
                    .ringBufferSizeInHalfOpenState(ringbufferByHopen)
                    .waitDurationInOpenState(Duration.ofSeconds(waitDuration))
                    .ignoreExceptions(annontion.ignoreExceptions())
                    .enableAutomaticTransitionFromOpenToHalfOpen()//如果置为true，当等待时间结束会自动由打开变为半开，若置为false，则需要一个请求进入来触发熔断器状态转换
                    .build();
            return circuitBreakerConfig;
        }

        private TimeLimiterConfig createTimeLimiterConfig(ClientTimeLimiter annotion) {
            long tlTimeOut = DEFAULT_TL_TIMEOUT;
            boolean tlCancel = DEFAUL_TL_CANEL;
            try{
                if(!StringUtils.isEmpty(annotion.timeoutDuration())) {
                    tlTimeOut = Long.parseLong(annotion.timeoutDuration());
                }
                tlCancel = annotion.cancelRunningFuture();
            }catch(Exception e) {
                //use default;
            }
            TimeLimiterConfig config = TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofMillis(tlTimeOut))
                    .cancelRunningFuture(tlCancel)
                    .build();
            return config;
        }

        private TimeLimiterConfig createTimeLimiterConfigbyProp(ClientTimeLimiter annotion, ApplicationContext applicationContext) {
            long tlTimeOut = DEFAULT_TL_TIMEOUT;
            boolean tlCancel = DEFAUL_TL_CANEL;
            try{
                String timeoutDurationKey = annotion.timeoutDuration();
                if(!org.apache.commons.lang.StringUtils.isEmpty(timeoutDurationKey)) {
                    //String timeoutDurationValue =  applicationContext.getEnvironment().getProperty(timeoutDurationKey);
                    String timeoutDurationValue =  this.getProperty(timeoutDurationKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(timeoutDurationValue)) {
                        tlTimeOut = Long.parseLong(timeoutDurationValue);
                    }
                }

                String cancelRunningFutureKey = annotion.timeoutDuration();
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
            TimeLimiterConfig config = TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofMillis(tlTimeOut))
                    .cancelRunningFuture(tlCancel)
                    .build();
            return config;
        }

        private RateLimiterConfig createRateLimiterConfig(ClientRateLimiter annotion) {
            int limitForPeriod = DEFAULT_LIMIT_FORPERIOD;
            long timeoutDuration = 1L;
            long limitRefreshPeriod = DEFAULT_LIMIT_REFRESHPERIOD;
            try{
                if(!StringUtils.isEmpty(annotion.limitForPeriod())) {
                    limitForPeriod = Integer.parseInt(annotion.limitForPeriod());
                }
                if(!StringUtils.isEmpty(annotion.limitRefreshPeriod())) {
                    limitRefreshPeriod = Long.parseLong(annotion.limitRefreshPeriod());
                }
            }catch(Exception e) {
                //use default;
            }
            RateLimiterConfig rateLimiterConfig = new RateLimiterConfig.Builder()
                    .limitForPeriod(limitForPeriod)
                    .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                    .limitRefreshPeriod(Duration.ofNanos(limitRefreshPeriod))
                    .build();
            return rateLimiterConfig;
        }

        private RateLimiterConfig createRateLimiterConfigByProp(ClientRateLimiter annotion, ApplicationContext applicationContext) {
            int limitForPeriod = DEFAULT_LIMIT_FORPERIOD;
            long timeoutDuration = 1L;
            long limitRefreshPeriod = DEFAULT_LIMIT_REFRESHPERIOD;
            try{
                String limitForPeriodKey = annotion.limitForPeriod();
                if(!org.apache.commons.lang.StringUtils.isEmpty(limitForPeriodKey)) {
                    String limitForPeriodValue =  this.getProperty(limitForPeriodKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(limitForPeriodValue)) {
                        limitForPeriod = Integer.parseInt(limitForPeriodValue);
                    }
                }

                String limitRefreshPeriodKey = annotion.limitRefreshPeriod();
                if(!org.apache.commons.lang.StringUtils.isEmpty(limitRefreshPeriodKey)) {
                    String limitRefreshPeriodValue = this.getProperty(limitRefreshPeriodKey, applicationContext);
                    if(!org.apache.commons.lang.StringUtils.isEmpty(limitRefreshPeriodValue)) {
                        limitRefreshPeriod = Long.parseLong(limitRefreshPeriodValue);
                    }
                }
            }catch(Exception e) {
                //use default;
            }
            RateLimiterConfig rateLimiterConfig = new RateLimiterConfig.Builder()
                    .limitForPeriod(limitForPeriod)
                    .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                    .limitRefreshPeriod(Duration.ofNanos(limitRefreshPeriod))
                    .build();
            return rateLimiterConfig;
        }

        private String getProperty(String text, ApplicationContext applicationContext) {
            String propertyValue = getEnvironmentProperty(text, applicationContext);
            return propertyValue;
        }

        private String getEnvironmentProperty(String text, ApplicationContext applicationContext) {
            if (!text.startsWith("$")) {
                return text;
            } else {
                String propertyKey = PROPERTY_KEYS.get(text) != null ? PROPERTY_KEYS.get(text).getKey() : null;

                if (propertyKey != null) {
                    String value = applicationContext.getEnvironment().getProperty(propertyKey);
                    if (value != null) {
                        return value;
                    } else {
                        return PROPERTY_KEYS.get(text).getValue() != null ? PROPERTY_KEYS.get(text).getValue() : text;
                    }
                } else {
                    Matcher matcher = PATTERN.matcher(text);

                    if (matcher.find()) {
                        String[] keyAndValue = org.apache.commons.lang3.StringUtils.split(matcher.group(1), ":");
                        String key = keyAndValue[0];
                        String defaultValue = null;
                        if (keyAndValue.length > 1) {
                            defaultValue = keyAndValue[1];
                        }
                        PROPERTY_KEYS.put(text, new ImmutablePair(key, defaultValue));
                        String value = applicationContext.getEnvironment().getProperty(key);
                        if (value != null) {
                            return value;
                        } else {
                            return defaultValue != null ? defaultValue : text;
                        }
                    }else{

                        String key = text.substring(1);

                        String value = applicationContext.getEnvironment().getProperty(key);
                        if (value != null) {
                            return value;
                        } else {
                            return key;
                        }
                    }


                }

            }

        }

        private boolean isUseValue(ApplicationContext applicationContext) {
            boolean usedValue = true;
            String pvalue = applicationContext.getEnvironment().getProperty(RES4J_USEFLAG);
            if(org.apache.commons.lang3.StringUtils.isNoneBlank(pvalue)
                    && pvalue.equalsIgnoreCase(KEY_UFLAG)) {
                usedValue = false;
            }
            return usedValue;
        }


    }


}
