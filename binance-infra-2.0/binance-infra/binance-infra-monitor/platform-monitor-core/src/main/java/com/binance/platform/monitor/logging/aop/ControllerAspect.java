package com.binance.platform.monitor.logging.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;

public interface ControllerAspect {

    void methodOrClassMonitorEnabledPointcut();

    Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    void logPostExecutionData(ProceedingJoinPoint proceedingJoinPoint, StopWatch timer, Object result,
        String returnType, RequestMapping methodRequestMapping, RequestMapping classRequestMapping, Throwable t);

    void logPreExecutionData(ProceedingJoinPoint proceedingJoinPoint, RequestMapping methodRequestMapping);

    void onException(JoinPoint joinPoint, Throwable t);

    void serialize(Object object, String objClassName, StringBuilder logMessage);
}
