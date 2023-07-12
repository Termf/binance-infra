package com.binance.platform.resilience4j.circuitbreaker.client;


import io.vavr.CheckedFunction1;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * author: sait xuc
 */
public class DefaultFallbackHandler <T> implements FallbackHandler<T> {

    private final T fallback;

    public DefaultFallbackHandler(T fallback) {
        this.fallback = fallback;
    }

    @Override
    public CheckedFunction1<Object[], Object> decorate(
            CheckedFunction1<Object[], Object> invocationCall,
            Method method,
            Predicate<Exception> filter) {
        validateFallback(fallback, method);
        Method fallbackMethod = getFallbackMethod(fallback, method);
        fallbackMethod.setAccessible(true);
        return args -> {
            try {
                return invocationCall.apply(args);
            } catch (Exception exception) {
                if (filter.test(exception)) {
                    return fallbackMethod.invoke(fallback, args);
                }
                throw exception;
            }
        };
    }

}
