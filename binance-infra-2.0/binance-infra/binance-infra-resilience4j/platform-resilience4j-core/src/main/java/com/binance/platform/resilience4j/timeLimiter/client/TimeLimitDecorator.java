package com.binance.platform.resilience4j.timeLimiter.client;

import com.binance.platform.resilience4j.openfeign.FeignDecorator;
import feign.InvocationHandlerFactory;
import feign.Target;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.CheckedFunction1;

import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.function.Supplier;

/***
 * author: sait xuc
 */
public class TimeLimitDecorator implements FeignDecorator {

    private TimeLimiter timeLimiter;
    private ScheduledExecutorService timeLimiterExecutorService;

    public TimeLimitDecorator(TimeLimiter timeLimiter, ScheduledExecutorService timeLimiterExecutorService) {
        this.timeLimiter = timeLimiter;
        this.timeLimiterExecutorService = timeLimiterExecutorService;
    }


    @Override
    public CheckedFunction1<Object[], Object> decorate(CheckedFunction1<Object[], Object> invocationCall, Method method, InvocationHandlerFactory.MethodHandler methodHandler, Target<?> target) {
        return args -> {
            //ExecutorService executorService = Executors.newSingleThreadExecutor();
            try{
                Callable call = () -> {
                    try {
                        return invocationCall.apply(args);
                    } catch (Throwable throwable) {
                        throw new Exception(throwable);
                    }
                };
                Supplier<Future<Object>> futureSupplier = () -> this.timeLimiterExecutorService.submit(call);
                Callable<Object> restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);
                return restrictedCall.call();
            }finally {
                //executorService.shutdown();
            }

        };

    }

}
