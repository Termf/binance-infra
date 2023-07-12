package com.binance.platform.resilience4j.openfeign;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import feign.Feign;
import feign.InvocationHandlerFactory;
import org.springframework.cloud.openfeign.FeignContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * author: ait xuc
 */
public class BinaceResilience4jFeign {

    private static final ScheduledExecutorService timeLimiterExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timeLimiterExecutorService.shutdown();
            try {
                if (!timeLimiterExecutorService.awaitTermination(5L, TimeUnit.SECONDS)) {
                    timeLimiterExecutorService.shutdownNow();
                }
            } catch (InterruptedException var1) {
                if (!timeLimiterExecutorService.isTerminated()) {
                    timeLimiterExecutorService.shutdownNow();
                }

                Thread.currentThread().interrupt();
            }

        }));
    }

    public static BinaceResilience4jFeign.Builder builder() {
        return new BinaceResilience4jFeign.Builder();
    }

    public static final class Builder extends Feign.Builder implements ApplicationContextAware {


        private BinanceResilience4jFactory resilience4jFactory = new BinanceResilience4jFactory.Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        /**
         * Will throw an {@link UnsupportedOperationException} exception.
         */
        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Feign build() {
            super.invocationHandlerFactory(
                    (target, dispatch) -> new BinanceDecoratorInvocationHandler(
                            target,
                            dispatch,
                            resilience4jFactory,
                            applicationContext,
                            feignContext,
                            timeLimiterExecutorService));
            return super.build();
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            this.feignContext = (FeignContext)this.applicationContext.getBean(FeignContext.class);
        }
    }

}
