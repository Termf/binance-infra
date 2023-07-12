package com.binance.platform.resilience4j.circuitbreaker.client;

import io.vavr.CheckedFunction1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * author: sait xuc
 */
public class FallbackFactory<T> implements FallbackHandler<T> {

    private final Function<Exception, T> fallbackSupplier;

    public FallbackFactory(Function<Exception, T> fallbackSupplier) {
        this.fallbackSupplier = fallbackSupplier;
    }

    @Override
    public CheckedFunction1<Object[], Object> decorate(
            CheckedFunction1<Object[], Object> invocationCall,
            Method method,
            Predicate<Exception> filter) {
        return args -> {
            try {
                return invocationCall.apply(args);
            } catch (Exception exception) {
                if (filter.test(exception)) {
                    T fallbackInstance = fallbackSupplier.apply(exception);
                    validateFallback(fallbackInstance, method);
                    Method fallbackMethod = getFallbackMethod(fallbackInstance, method);
                    try {
                        return fallbackMethod.invoke(fallbackInstance, args);
                    } catch (InvocationTargetException e) {
                        // Rethrow the exception thrown in the fallback wrapped by InvocationTargetException
                        throw e.getCause();
                    }
                }
                throw exception;
            }
        };
    }
}
