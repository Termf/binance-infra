package com.binance.platform.monitor.health;

import java.lang.management.ManagementFactory;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class HealthCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckAspect.class);

    private final long startTime;

    public HealthCheckAspect() {
        this.startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    }

    @Pointcut("execution(* org.springframework.boot.actuate.health.HealthEndpoint.*(..))")
    public void healthCheckPointcut() {}

    @Before(value = "healthCheckPointcut()")
    public void beforeMethod(JoinPoint joinPoint) {

    }

    @AfterReturning(value = "healthCheckPointcut()", returning = "health")
    public void afterReturning(JoinPoint joinPoint, Object health) {
        String healthCheckResult = String.valueOf(health);
        boolean isDown = StringUtils.containsAny(healthCheckResult, "DOWN", "OUT_OF_SERVICE");
        if (isDown) {
            LOGGER.info("*****health check result is: " + healthCheckResult + "*****");
        } else {
            long methodTime = System.currentTimeMillis();
            if (methodTime - startTime <= 1800000) {
                LOGGER.info("*****health check result is: " + healthCheckResult + "*****");
            }
        }

    }

}
