package com.binance.platform.env;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

@Order(value = 0)
@Slf4j
public class EnvApplictionRunListener implements SpringApplicationRunListener {

    public EnvApplictionRunListener(SpringApplication application, String[] args) {
        try {
            if (EnvUtil.isJdk11()) {
                Class<ForkJoinPool> clazz = ForkJoinPool.class;
                Field field = ReflectionUtils.findField(clazz, "factory");
                ReflectionUtils.makeAccessible(field);
                Field modifiersField = ReflectionUtils.findField(Field.class, "modifiers");
                ReflectionUtils.makeAccessible(modifiersField);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                ReflectionUtils.setField(field, ForkJoinPool.commonPool(),
                    new ForkJoinCommonPoolContextAwareWorkerThreadFactory());
            }
        } catch (Exception e) {
            log.warn("set forkjoinpool thread class loader error: ", e);
        }
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        EnvUtil.env = environment;
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        // TODO Auto-generated method stub

    }

    public class ForkJoinCommonPoolContextAwareWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        @Override
        public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new ForkJoinCommonPoolContextAwareWorkerThread(pool);
        }
    }

    public class ForkJoinCommonPoolContextAwareWorkerThread extends ForkJoinWorkerThread {
        public ForkJoinCommonPoolContextAwareWorkerThread(final ForkJoinPool pool) {
            super(pool);
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            // set context ClassLoader to ThreadContextClassLoader
            setContextClassLoader(contextClassLoader != null ? contextClassLoader : ClassLoader.getSystemClassLoader());
        }
    }

}
