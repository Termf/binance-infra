package com.binance.platform.resilience4j.openfeign;

import com.binance.platform.resilience4j.bulkhead.BinanceBulkheadRegister;
import com.binance.platform.resilience4j.circuitbreaker.client.DefaultFallbackHandler;
import com.binance.platform.resilience4j.circuitbreaker.client.FallbackDecorator;
import com.binance.platform.resilience4j.circuitbreaker.client.FallbackFactory;
import com.binance.platform.resilience4j.timeLimiter.client.TimeLimitDecorator;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.vavr.CheckedFunction1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * author: sait xuc
 */
public class FeignDecorators implements FeignDecorator {

    private final List<FeignDecorator> decorators;

    private FeignDecorators(List<FeignDecorator> decorators) {
        this.decorators = decorators;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CheckedFunction1<Object[], Object> decorate(CheckedFunction1<Object[], Object> fn,
                                                       Method method, MethodHandler methodHandler, Target<?> target) {
        CheckedFunction1<Object[], Object> decoratedFn = fn;
        for (final FeignDecorator decorator : decorators) {
            decoratedFn = decorator.decorate(decoratedFn, method, methodHandler, target);
        }
        return decoratedFn;
    }

    public static final class Builder {

        private final List<FeignDecorator> decorators = new ArrayList<>();


        public Builder withCircuitBreaker(CircuitBreaker circuitBreaker) {
            decorators
                    .add((fn, m, mh, t) -> CircuitBreaker.decorateCheckedFunction(circuitBreaker, fn));
            return this;
        }

        public Builder withTimeLimit(TimeLimiter timeLimit, ScheduledExecutorService timeLimiterExecutorService) {
            decorators.add(new TimeLimitDecorator(timeLimit, timeLimiterExecutorService));
            return this;
        }

        public Builder withRateLimiter(RateLimiter rateLimiter) {
            decorators.add((fn, m, mh, t) -> RateLimiter.decorateCheckedFunction(rateLimiter, fn));
            return this;
        }

        public Builder withFallback(Object fallback) {
            decorators.add(new FallbackDecorator<>(new DefaultFallbackHandler<>(fallback)));
            return this;
        }


        public Builder withFallbackFactory(Function<Exception, ?> fallbackFactory) {
            decorators.add(new FallbackDecorator<>(new FallbackFactory<>(fallbackFactory)));
            return this;
        }

        public Builder withFallback(Object fallback, Class<? extends Exception> filter) {
            decorators.add(new FallbackDecorator<>(new DefaultFallbackHandler<>(fallback), filter));
            return this;
        }

        public Builder withFallbackFactory(Function<Exception, ?> fallbackFactory,
                                           Class<? extends Exception> filter) {
            decorators.add(new FallbackDecorator<>(new FallbackFactory<>(fallbackFactory), filter));
            return this;
        }

        public Builder withFallback(Object fallback, Predicate<Exception> filter) {
            decorators.add(new FallbackDecorator<>(new DefaultFallbackHandler<>(fallback), filter));
            return this;
        }

        public Builder withFallbackFactory(Function<Exception, ?> fallbackFactory,
                                           Predicate<Exception> filter) {
            decorators.add(new FallbackDecorator<>(new FallbackFactory<>(fallbackFactory), filter));
            return this;
        }


        public Builder withBulkhead(Bulkhead bulkhead, BinanceBulkheadRegister binanceBulkheadRegister) {
            decorators.add((fn, m, mh, t) -> BinanceBulkheadRegister.BinanceBulkheadFun.decorateCheckedFunction(bulkhead, fn, binanceBulkheadRegister));
            return this;
        }


        public FeignDecorators build() {
            return new FeignDecorators(decorators);
        }

    }
}
