package com.binance.platform;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class TestContextUtils {

    public static GenericApplicationContext load(Class<?> config, String  ... properties) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(config);
        TestPropertyValues.of(properties).applyTo(ctx);
        ctx.refresh();
        return ctx;
    }
}
