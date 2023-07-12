package com.binance.platform.resilience4j.circuitbreaker.client;

import com.binance.platform.resilience4j.openfeign.FeignDecorator;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;
import io.vavr.CheckedFunction1;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * author: sait xuc
 */
public class FallbackDecorator <T> implements FeignDecorator {

    private final FallbackHandler<T> fallback;
    private final Predicate<Exception> filter;

    /**
     * Creates a fallback that will be called for every {@link Exception}.
     */
    public FallbackDecorator(FallbackHandler<T> fallback) {
        this(fallback, ex -> true);
    }

    /**
     * Creates a fallback that will only be called for the specified {@link Exception}.
     */
    public FallbackDecorator(FallbackHandler<T> fallback, Class<? extends Exception> filter) {
        this(fallback, filter::isInstance);
        requireNonNull(filter, "Filter cannot be null!");
    }

    /**
     * Creates a fallback that will only be called if the specified {@link Predicate} returns
     * <code>true</code>.
     */
    public FallbackDecorator(FallbackHandler<T> fallback, Predicate<Exception> filter) {
        this.fallback = requireNonNull(fallback, "Fallback cannot be null!");
        this.filter = requireNonNull(filter, "Filter cannot be null!");
    }

    /**
     * Calls the fallback if the invocationCall throws an {@link Exception}.
     *
     * @throws IllegalArgumentException if the fallback object does not have a corresponding
     *                                  fallback method.
     */
    @Override
    public CheckedFunction1<Object[], Object> decorate(
            CheckedFunction1<Object[], Object> invocationCall,
            Method method,
            MethodHandler methodHandler,
            Target<?> target) {
        return fallback.decorate(invocationCall, method, filter);
    }
}
