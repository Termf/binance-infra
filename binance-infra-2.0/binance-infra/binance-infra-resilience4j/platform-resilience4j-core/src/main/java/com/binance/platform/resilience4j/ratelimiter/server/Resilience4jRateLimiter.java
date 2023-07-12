package com.binance.platform.resilience4j.ratelimiter.server;

import static com.ctrip.framework.apollo.enums.PropertyChangeType.ADDED;
import static com.ctrip.framework.apollo.enums.PropertyChangeType.MODIFIED;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.master.models.APIResponse.Type;
import com.binance.platform.common.MyJsonUtil;
import com.binance.platform.resilience4j.PlaceHolderResilience4j;
import com.binance.platform.resilience4j.ratelimiter.RateLimiterStrategy;
import com.binance.platform.resilience4j.ratelimiter.ServerRateLimiter;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.collect.Lists;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class Resilience4jRateLimiter extends PlaceHolderResilience4j implements ConfigChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resilience4jRateLimiter.class);

    private static final Map<String, RateLimiter> RESILIENCE4J_HANDLERMETHOD_RATELIMITER = ExpiringMap.builder()//
        .variableExpiration()//
        .expirationPolicy(ExpirationPolicy.ACCESSED)//
        .expiration(60, TimeUnit.SECONDS)//
        .maxSize(500)//
        .build();

    private static class Resilience4jRateLimiterHoler {
        private static Resilience4jRateLimiter instance = new Resilience4jRateLimiter();
    }

    private Resilience4jRateLimiter() {
        ConfigService.getAppConfig().addChangeListener(this);
    }

    public static Resilience4jRateLimiter getInstance() {
        return Resilience4jRateLimiterHoler.instance;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            String clazzAndMethodName = clazz.getName() + "_" + method.getName();
            ServerRateLimiter methodLimit =
                AnnotationUtils.getAnnotation(handlerMethod.getMethod(), ServerRateLimiter.class);
            ServerRateLimiter classlimit =
                AnnotationUtils.getAnnotation(handlerMethod.getMethod().getDeclaringClass(), ServerRateLimiter.class);
            if (classlimit != null && methodLimit == null) {
                return doRateLimiter(response, clazzAndMethodName, classlimit);
            }
            if (methodLimit != null) {
                return doRateLimiter(response, clazzAndMethodName, methodLimit);
            }

        }
        return true;
    }

    private boolean doRateLimiter(HttpServletResponse response, String clazzAndMethodName, ServerRateLimiter classlimit)
        throws IOException {
        List<RateLimiterStrategy> strategyInstanceList = getStrategyInstance(classlimit.strategy());
        for (RateLimiterStrategy strategyInstance : strategyInstanceList) {
            RateLimiter rateLimiter =
                RESILIENCE4J_HANDLERMETHOD_RATELIMITER.get(clazzAndMethodName + strategyInstance.strategy());
            if (rateLimiter == null) {
                buildRateLimiter(classlimit, clazzAndMethodName);
                rateLimiter =
                    RESILIENCE4J_HANDLERMETHOD_RATELIMITER.get(clazzAndMethodName + strategyInstance.strategy());
            }
            if (rateLimiter == null) {
                continue;
            }
            boolean permission = rateLimiter.acquirePermission(1);
            if (permission) {
                return true;
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                APIResponse<Object> apiResp = new APIResponse<Object>(APIResponse.Status.ERROR, Type.GENERAL,
                    GeneralCode.TOO_MANY_REQUESTS.getCode(), "has trigger ratlimiter", null);
                try (PrintWriter out = response.getWriter()) {
                    out.print(MyJsonUtil.toJson(apiResp));
                }
                LOGGER.info("has trigger ratlimiter");
                return false;
            }
        }
        return true;
    }

    private void buildRateLimiter(ServerRateLimiter limit, String clazzAndMethodName) {
        List<RateLimiterStrategy> strategyInstanceList = this.getStrategyInstance(limit.strategy());
        for (RateLimiterStrategy strategy : strategyInstanceList) {
            String key = clazzAndMethodName + strategy.strategy();
            RateLimiterConfig config = RateLimiterConfig.custom().timeoutDuration(strategy.timeoutDuration())
                .limitRefreshPeriod(Duration.ofSeconds(getProperty(limit.limitRefreshPeriod())))
                .limitForPeriod(getProperty(limit.limitForPeriod())).build();
            RateLimiter rateLimiter = RateLimiter.of(key, config);
            RESILIENCE4J_HANDLERMETHOD_RATELIMITER.put(key, rateLimiter);

        }
    }

    private List<RateLimiterStrategy> getStrategyInstance(Class<? extends RateLimiterStrategy>[] strategyClass) {
        List<RateLimiterStrategy> strategyInstances = Lists.newArrayList();
        for (Class<? extends RateLimiterStrategy> clazz : strategyClass) {
            RateLimiterStrategy strategy = applicationContext.getBean(clazz);
            strategyInstances.add(strategy);
        }
        strategyInstances.sort(Comparator.comparing(RateLimiterStrategy::order));
        return strategyInstances;
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        for (String key : changeEvent.changedKeys()) {
            if (StringUtils.containsIgnoreCase(key, "ratelimit")) {
                ConfigChange change = changeEvent.getChange(key);
                if (change.getChangeType() == MODIFIED || change.getChangeType() == ADDED) {
                    RESILIENCE4J_HANDLERMETHOD_RATELIMITER.clear();
                    break;
                }
            }
        }
    }

}
