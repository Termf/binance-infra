package com.binance.platform.monitor.metric.hazelcast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.binance.platform.monitor.metric.CustomizerTag;
import com.hazelcast.core.IMap;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.cache.HazelcastCacheMetrics;

@Aspect
public class HazelCastAspect {
    CustomizerTag platformTag;

    public HazelCastAspect(CustomizerTag platformTag) {
        this.platformTag = platformTag;
    }

    @Around("execution(* com.hazelcast.core.HazelcastInstance.getMap(..))")
    public IMap aroundGetMap(ProceedingJoinPoint pjp) throws Throwable {
        IMap iMap = (IMap)pjp.proceed();
        try {
            HazelcastCacheMetrics.monitor(Metrics.globalRegistry, iMap, platformTag.getTags());
        } catch (Throwable ignore) {
        }
        return iMap;
    }
}
