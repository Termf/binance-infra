package com.binance.platform.resilience4j.circuitbreaker.client;


import io.vavr.CheckedFunction1;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * author: sait xuc
 */
public interface FallbackHandler <T> {

    CheckedFunction1<Object[], Object> decorate(CheckedFunction1<Object[], Object> invocationCall,
                                                Method method, Predicate<Exception> filter);

    default void validateFallback(T fallback, Method method) {
        if (fallback.getClass().isAssignableFrom(method.getDeclaringClass())) {
            throw new IllegalArgumentException("Cannot use the fallback ["
                    + fallback.getClass() + "] for ["
                    + method.getDeclaringClass() + "]!");
        }
    }

    default Method getFallbackMethod(T fallbackInstance, Method method) {
        Method fallbackMethod;
        try {
            fallbackMethod = fallbackInstance.getClass()
                    .getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Cannot use the fallback ["
                    + fallbackInstance.getClass() + "] for ["
                    + method.getDeclaringClass() + "]", e);
        }
        return fallbackMethod;
    }



}
