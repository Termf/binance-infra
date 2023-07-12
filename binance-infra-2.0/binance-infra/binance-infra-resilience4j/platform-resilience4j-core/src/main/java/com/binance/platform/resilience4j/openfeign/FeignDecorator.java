package com.binance.platform.resilience4j.openfeign;

import feign.InvocationHandlerFactory;
import feign.Target;
import io.vavr.CheckedFunction1;
import java.lang.reflect.Method;

/**
 * author: sait xuc
 */
@FunctionalInterface
public interface FeignDecorator {

    CheckedFunction1<Object[], Object> decorate(CheckedFunction1<Object[], Object> invocationCall,
                                                Method method, InvocationHandlerFactory.MethodHandler methodHandler,
                                                Target<?> target);

}
