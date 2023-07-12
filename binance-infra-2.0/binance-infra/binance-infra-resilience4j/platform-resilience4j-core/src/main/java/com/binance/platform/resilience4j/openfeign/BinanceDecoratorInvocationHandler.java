package com.binance.platform.resilience4j.openfeign;

import java.lang.reflect.InvocationHandler;
import feign.Target;
import java.lang.reflect.Proxy;
import java.lang.reflect.Method;

import feign.Util;
import io.vavr.CheckedFunction1;
import feign.InvocationHandlerFactory;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import static feign.Util.checkNotNull;

/**
 * author: sait xuc
 */
public class BinanceDecoratorInvocationHandler implements InvocationHandler {

    private final Target<?> target;
    private final Map<Method, CheckedFunction1<Object[], Object>> decoratedDispatch;
    private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;
    private final ScheduledExecutorService timeLimiterExecutorService;

    public BinanceDecoratorInvocationHandler(Target<?> target,
                                             Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,
                                             BinanceResilience4jFactory resilience4jFactory,
                                             ApplicationContext applicationContext,
                                             FeignContext feignContext,
                                             ScheduledExecutorService timeLimiterExecutorService) {
        this.target = checkNotNull(target, "target");
        checkNotNull(dispatch, "dispatch");
        this.timeLimiterExecutorService = timeLimiterExecutorService;
        this.dispatch = (Map) Util.checkNotNull(dispatch, "dispatch for %s", new Object[]{target});
        this.decoratedDispatch = decorateMethodHandlers(dispatch, resilience4jFactory, target, applicationContext, feignContext);
    }

    private Map<Method, CheckedFunction1<Object[], Object>> decorateMethodHandlers(
                                        Map<Method,
                                        InvocationHandlerFactory.MethodHandler> dispatch,
                                        BinanceResilience4jFactory resilience4jFactory,
                                        Target<?> target,
                                        ApplicationContext applicationContext,
                                        FeignContext feignContext) {
        final Map<Method, CheckedFunction1<Object[], Object>> map = new HashMap<>();
        for (final Map.Entry<Method, InvocationHandlerFactory.MethodHandler> entry : dispatch.entrySet()) {
            final Method method = entry.getKey();
            final InvocationHandlerFactory.MethodHandler methodHandler = entry.getValue();
            if(methodHandler != null) {
                map.put(method, resilience4jFactory.create(target,method, applicationContext, feignContext, timeLimiterExecutorService).decorate(methodHandler::invoke, method, methodHandler, target));
            }
        }
        return map;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        switch (method.getName()) {
            case "equals":
                return equals(args.length > 0 ? args[0] : null);

            case "hashCode":
                return hashCode();

            case "toString":
                return toString();

            default:
                break;
        }
        return decoratedDispatch.get(method).apply(args);
    }

    @Override
    public boolean equals(Object obj) {
        Object compareTo = obj;
        if (compareTo == null) {
            return false;
        }
        if (Proxy.isProxyClass(compareTo.getClass())) {
            compareTo = Proxy.getInvocationHandler(compareTo);
        }
        if (compareTo instanceof BinanceDecoratorInvocationHandler) {
            final BinanceDecoratorInvocationHandler other = (BinanceDecoratorInvocationHandler) compareTo;
            return target.equals(other.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
