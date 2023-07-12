package com.binance.platform.resilience4j.ratelimiter.server;

import static com.ctrip.framework.apollo.enums.PropertyChangeType.ADDED;
import static com.ctrip.framework.apollo.enums.PropertyChangeType.MODIFIED;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.method.HandlerMethod;

import com.binance.master.error.GeneralCode;
import com.binance.master.models.APIResponse;
import com.binance.master.models.APIResponse.Type;
import com.binance.platform.common.MyJsonUtil;
import com.binance.platform.resilience4j.PlaceHolderResilience4j;
import com.binance.platform.resilience4j.ratelimiter.RateLimiterStrategy;
import com.binance.platform.resilience4j.ratelimiter.ServerRateLimiter;
import com.binance.platform.resilience4j.ratelimiter.server.redis.RedisLuaSupport;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.collect.Lists;

public class RedisRateLimiter extends PlaceHolderResilience4j implements ConfigChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRateLimiter.class);

    private DefaultRedisScript<Long> redisScript;

    private List<String> keyList;

    private static class RedisRateLimiterHoler {
        private static RedisRateLimiter instance = new RedisRateLimiter();
    }

    private RedisRateLimiter() {
        redisScript = new DefaultRedisScript<Long>();
        redisScript.setScriptSource(
            new ResourceScriptSource(new ClassPathResource("ratelimiter/redis-ratelimiter-counter.lua")));
        redisScript.setResultType(Long.class);
        LOGGER.debug("init ratelimiter lua script success:{}", redisScript.getScriptAsString());
        ConfigService.getAppConfig().addChangeListener(this);
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            try {
                Method method = handlerMethod.getMethod();
                Class<?> clazz = method.getDeclaringClass();
                ServerRateLimiter methodLimit = AnnotationUtils.getAnnotation(method, ServerRateLimiter.class);
                ServerRateLimiter classlimit = AnnotationUtils.getAnnotation(clazz, ServerRateLimiter.class);
                List<String> key = Lists.newArrayList();
                Integer limit = 0;
                Integer expire = 0;
                if (classlimit != null && methodLimit == null) {
                    List<RateLimiterStrategy> strategyInstanceList = this.strategyInstance(classlimit.strategy());
                    StringBuffer sb = new StringBuffer(clazz.getName() + "_" + method.getName());
                    for (RateLimiterStrategy strategy : strategyInstanceList) {
                        sb.append(strategy.strategy());
                    }
                    limit = getProperty(classlimit.limitForPeriod());
                    expire = getProperty(classlimit.limitRefreshPeriod());
                    key.add(sb.toString());
                }
                if (methodLimit != null) {
                    List<RateLimiterStrategy> strategyInstanceList = this.strategyInstance(methodLimit.strategy());
                    StringBuffer sb = new StringBuffer(clazz.getName() + "_" + method.getName());
                    for (RateLimiterStrategy strategy : strategyInstanceList) {
                        sb.append(strategy.strategy());
                    }
                    limit = getProperty(methodLimit.limitForPeriod());
                    expire = getProperty(methodLimit.limitRefreshPeriod());
                    key.add(sb.toString());
                }
                Object result = RedisLuaSupport.evalScript(applicationContext, redisScript, key,
                    Lists.newArrayList(limit.toString(), expire.toString()));
                this.keyList = key;
                Long intergerResult = (Long)result;
                if (intergerResult > 0) {
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

            } catch (Throwable e) {
                LOGGER.error("Redis RateLimit occur exception,the message is {},and will fallback to use resilience4j",
                    e.getMessage());
                Resilience4jRateLimiter rateLimiter = Resilience4jRateLimiter.getInstance();
                rateLimiter.setApplicationContext(applicationContext);
                return rateLimiter.preHandle(request, response, handlerMethod);
            }

        }
        return true;
    }

    private List<RateLimiterStrategy> strategyInstance(Class<? extends RateLimiterStrategy>[] strategyClass) {
        List<RateLimiterStrategy> strategyInstances = Lists.newArrayList();
        for (Class<? extends RateLimiterStrategy> clazz : strategyClass) {
            strategyInstances.add(applicationContext.getBean(clazz));
        }
        strategyInstances.sort(Comparator.comparing(RateLimiterStrategy::order));
        return strategyInstances;
    }

    public static RedisRateLimiter getInstance() {
        return RedisRateLimiterHoler.instance;
    }

    @Override
    public void onChange(ConfigChangeEvent changeEvent) {
        for (String key : changeEvent.changedKeys()) {
            if (StringUtils.containsIgnoreCase(key, "ratelimit")) {
                ConfigChange change = changeEvent.getChange(key);
                if (change.getChangeType() == MODIFIED || change.getChangeType() == ADDED) {
                    RedisTemplate redisTemplate = RedisLuaSupport.getRedisTemplate(applicationContext);
                    if (redisTemplate != null && this.keyList != null) {
                        redisTemplate.delete(this.keyList);
                    }
                    break;
                }
            }
        }
    }

}
