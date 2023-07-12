package com.binance.platform.monitor.metric;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.sizeof.SizeOf;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.monitor.Monitors;

public class RedisCommandBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private static final SizeOf SIZEOF = SizeOf.newInstance();
    private static final RedisSerializer<String> REDISSERIALIZER = new StringRedisSerializer();
    private static final ExecutorService RECORD_REDIS_METRCIS = Executors.newSingleThreadExecutor();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisTemplate || bean instanceof StringRedisTemplate) {
            RedisTemplate redisTemplate = (RedisTemplate)bean;
            ConnectionMetrcisInterceptor interceptor = new ConnectionMetrcisInterceptor();
            redisTemplate.opsForValue();
            redisTemplate.opsForList();
            redisTemplate.opsForSet();
            redisTemplate.opsForZSet();
            redisTemplate.opsForGeo();
            redisTemplate.opsForHash();
            redisTemplate.opsForHyperLogLog();
            createProxyHandlers(redisTemplate, interceptor);
        }
        return bean;
    }

    private void createProxyHandlers(RedisTemplate redisTemplate, ConnectionMetrcisInterceptor interceptor) {
        resetOperations(redisTemplate, ValueOperations.class, "valueOps", interceptor);
        resetOperations(redisTemplate, ListOperations.class, "listOps", interceptor);
        resetOperations(redisTemplate, SetOperations.class, "setOps", interceptor);
        resetOperations(redisTemplate, ZSetOperations.class, "zSetOps", interceptor);
        resetOperations(redisTemplate, GeoOperations.class, "geoOps", interceptor);
        resetOperations(redisTemplate, HyperLogLogOperations.class, "hllOps", interceptor);
    }

    private void resetOperations(RedisTemplate redisTemplate, Class clazz, String name,
        ConnectionMetrcisInterceptor interceptor) {
        try {
            Field field = ReflectionUtils.findField(RedisTemplate.class, name, clazz);
            ReflectionUtils.makeAccessible(field);
            Object object = ReflectionUtils.getField(field, redisTemplate);
            ProxyFactory proxyFactory = new ProxyFactory(object);
            proxyFactory.addAdvice(interceptor);
            Object proxy = proxyFactory.getProxy();
            field.set(redisTemplate, proxy);
        } catch (Exception e) {

        }

    }

    public static long sizeOfArgs(Object[] args) {
        long size = SIZEOF.sizeOf(args);
        return size;
    }

    public static String strOfArg(Object obj) {
        String key = null;
        if (obj.getClass() == byte[].class) {
            key = REDISSERIALIZER.deserialize((byte[])obj);
        } else {
            key = String.valueOf(obj);
        }
        return key;
    }

    public static int getParameterNameJava8(Method method, String key) {
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            String name = params[i].getName();
            if (StringUtils.equals(name, key)) {
                return i;
            }
        }
        return 0;
    }

    public class ConnectionMetrcisInterceptor
        implements MethodInterceptor, org.springframework.cglib.proxy.MethodInterceptor {
        private static final String EQUALS = "equals";

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            String methodName = method.getName();
            if (EQUALS.equals(methodName) || args == null || args.length == 0) {
                return method.invoke(obj, args);
            } else {
                Object result;
                long start = System.nanoTime();
                try {
                    result = method.invoke(obj, args);
                    long cost = System.nanoTime() - start;
                    afterCommand(method, args, cost, null);
                } catch (Throwable e) {
                    long cost = System.nanoTime() - start;
                    afterCommand(method, args, cost, e);
                    throw e;
                }
                return result;
            }
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), null);
        }

        public void afterCommand(Method method, Object[] args, long costNano, Throwable throwable) {
            RECORD_REDIS_METRCIS.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        // redis的key的话有可能太多，在特定的应用有几万个，这个会导致tag暴增，撑爆应用内存
                        // int key_index = getParameterNameJava8(method, "key");
                        // String key = strOfArg(args[key_index]);
                        if (args != null && args.length > 0) {
                            long argSize = sizeOfArgs(args);
                            // Monitors.recordTime("redis.command.cost", costNano, TimeUnit.NANOSECONDS,
                            // "command",
                            // method.getName(), "key", key, "exception",
                            // null == throwable ? "none" : throwable.getClass().getSimpleName());
                            // Monitors.summary("redis.command.bytes.length", argSize, "command",
                            // method.getName(), "key",
                            // key,
                            // "exception", null == throwable ? "none" : throwable.getMessage());
                            Monitors.recordTime("redis.command.cost", costNano, TimeUnit.NANOSECONDS, "command",
                                null == method ? "none" : method.getName(), "exception",
                                null == throwable ? "none" : throwable.getClass().getSimpleName());
                            Monitors.summary("redis.command.bytes.length", argSize, "command",
                                null == method ? "none" : method.getName(), "exception",
                                null == throwable ? "none" : throwable.getClass().getSimpleName());
                        }
                    } catch (Throwable e) {
                    }
                }
            });
        }
    }

}
