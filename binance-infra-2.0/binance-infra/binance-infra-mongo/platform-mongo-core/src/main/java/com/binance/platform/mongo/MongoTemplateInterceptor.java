package com.binance.platform.mongo;


import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.binance.platform.mongo.preference.MongoPreferenceTemplate;
import com.binance.platform.mongo.preference.MongoReadPreference;
import com.mongodb.ReadPreference;
import lombok.extern.slf4j.Slf4j;

/**
 * mongo template interceptor for modify some properties,such as: readpreference
 */
@Aspect
@Component
@Slf4j
public class MongoTemplateInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 定义拦截规则
     */
    @Pointcut(value = "@annotation(com.binance.platform.mongo.preference.MongoReadPreference)")
    public void annotationPointcut() {}


    @Around("annotationPointcut()")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = getMethod(proceedingJoinPoint);
        MongoPreferenceTemplate mongoTemplate = null;

        try {
            // need modify read preference for this query
            MongoReadPreference mongoReadPreference = method.getAnnotation(MongoReadPreference.class);
            ReadPreference readPreference = ReadPreference.valueOf(mongoReadPreference.type());
            if (readPreference != null) {
                // new read preference is valid
                mongoTemplate =this.applicationContext.getBean(MongoPreferenceTemplate.class);
                if(mongoTemplate!=null){
                    mongoTemplate.setReadPreference(readPreference);
                }
            }
        } catch (Exception ex) {
            log.error("MongoTemplateInterceptor exception: ", ex.getCause());
        }

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            if (mongoTemplate != null) {
                mongoTemplate.removeReadPreference();
            }
        }
    }


    public Method getMethod(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        return pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), methodSignature.getParameterTypes());
    }
}
