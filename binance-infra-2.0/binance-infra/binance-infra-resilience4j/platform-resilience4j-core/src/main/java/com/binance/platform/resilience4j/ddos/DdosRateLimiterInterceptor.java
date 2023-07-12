package com.binance.platform.resilience4j.ddos;

import static com.ctrip.framework.apollo.enums.PropertyChangeType.ADDED;
import static com.ctrip.framework.apollo.enums.PropertyChangeType.MODIFIED;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.binance.master.constant.Constant;
import com.binance.master.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.master.models.APIResponse.Type;
import com.binance.master.utils.RequestIpUtil;
import com.binance.platform.common.MyJsonUtil;
import com.binance.platform.resilience4j.PlaceHolderResilience4j;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class DdosRateLimiterInterceptor extends PlaceHolderResilience4j
    implements ConfigChangeListener, HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdosRateLimiterInterceptor.class);

    private final ApplicationContext applicationContext;
    private final DdosWhiteList ddosWhiteList;

    private static final Map<String, RateLimiter> RESILIENCE4J_HANDLERMETHOD_RATELIMITER = ExpiringMap.builder()//
        .variableExpiration()//
        .expirationPolicy(ExpirationPolicy.ACCESSED)//
        .expiration(60, TimeUnit.SECONDS)//
        .maxSize(500)//
        .build();

    private DdosReactor ddosReactor;

    public DdosRateLimiterInterceptor(ApplicationContext applicationContext, DdosWhiteList ddosWhiteList) {
        this.applicationContext = applicationContext;
        this.ddosWhiteList = ddosWhiteList;
        super.setApplicationContext(applicationContext);
        ConfigService.getAppConfig().addChangeListener(this);
        try {
            ddosReactor = new DdosReactor(applicationContext);
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
            ddosReactor = null;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method method = handlerMethod.getMethod();
            Class<?> clazz = method.getDeclaringClass();
            String clazzAndMethodName = clazz.getName() + "_" + method.getName();
            ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null && requestAttributes.getRequest() != null) {
                HttpServletRequest servletRequest = requestAttributes.getRequest();
                String requestIp = RequestIpUtil.getIpAddress(servletRequest);
                String userId = servletRequest.getHeader(Constant.HEADER_USER_ID);
                if (ddosWhiteList.isInWhitelist(requestIp)) {
                    return true;
                }
                Ddos methodLimit = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), Ddos.class);
                Ddos classlimit =
                    AnnotationUtils.getAnnotation(handlerMethod.getMethod().getDeclaringClass(), Ddos.class);
                if (classlimit != null && methodLimit == null) {
                    return doRateLimiter(response, clazzAndMethodName, requestIp, classlimit, userId);
                }
                if (methodLimit != null) {
                    return doRateLimiter(response, clazzAndMethodName, requestIp, methodLimit, userId);
                }
            }
            return true;
        }
        return true;
    }

    private boolean doRateLimiter(HttpServletResponse response, String clazzAndMethodName, String requestIp,
                                  Ddos ddoslimit, String userId) throws IOException {
        if (ddoslimit.strategy() != SystemDefaultStrategy.class) {
            DdosStrategy ddosStrategy = applicationContext.getBean(ddoslimit.strategy());
            if (ddosStrategy.isDdos()) {
                sendRequestIpWaf(requestIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                APIResponse<Object> apiResp = new APIResponse<Object>(APIResponse.Status.ERROR, Type.GENERAL,
                    GeneralCode.TOO_MANY_REQUESTS.getCode(), "has trigger rate limiter", null);
                try (PrintWriter out = response.getWriter()) {
                    out.print(MyJsonUtil.toJson(apiResp));
                }
                LOGGER.info("has trigger rate limiter, ip:{}, host:{}", requestIp, WebUtils.getHeader(HttpHeaders.HOST));
                return false;
            } else {
                return true;
            }
        } else {
            String key = clazzAndMethodName + ":" + requestIp + ":" + userId;
            RateLimiter rateLimiter = RESILIENCE4J_HANDLERMETHOD_RATELIMITER.get(key);
            if (rateLimiter == null) {
                RateLimiterConfig config =
                    RateLimiterConfig.custom().timeoutDuration(Duration.ofMillis(ddoslimit.timeoutDuration()))
                        .limitRefreshPeriod(Duration.ofSeconds(getProperty(ddoslimit.limitRefreshPeriod())))
                        .limitForPeriod(getProperty(ddoslimit.limitForPeriod())).build();
                rateLimiter = RateLimiter.of(key, config);
                RESILIENCE4J_HANDLERMETHOD_RATELIMITER.put(key, rateLimiter);
            }
            boolean permission = rateLimiter.acquirePermission(1);
            if (permission) {
                return true;
            } else {
                sendRequestIpWaf(requestIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                APIResponse<Object> apiResp = new APIResponse<Object>(APIResponse.Status.ERROR, Type.GENERAL,
                    GeneralCode.TOO_MANY_REQUESTS.getCode(), "has trigger ratlimiter", null);
                try (PrintWriter out = response.getWriter()) {
                    out.print(MyJsonUtil.toJson(apiResp));
                }
                LOGGER.info("has trigger rate limiter, ip:{}, host:{}", requestIp, WebUtils.getHeader(HttpHeaders.HOST));
                return false;
            }
        }
    }

    public void sendRequestIpWaf(String requestIp) {
        if (ddosReactor != null) {
            ddosReactor.process(requestIp);
        } else {
            LOGGER.error("can not send requestIp to waf,maybe AccessDenied to aws sqs");
        }
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        for (String key : changeEvent.changedKeys()) {
            if (StringUtils.containsIgnoreCase(key, "ddos")) {
                ConfigChange change = changeEvent.getChange(key);
                if (change.getChangeType() == MODIFIED || change.getChangeType() == ADDED) {
                    RESILIENCE4J_HANDLERMETHOD_RATELIMITER.clear();
                    break;
                }
            }
        }
    }

}
