package com.binance.platform.env;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import com.binance.master.constant.Constant;

public class EnvCondition {

    public static class DevCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return EnvUtil.isDev() || EnvUtil.isQa();
        }
    }

    public static class ProdCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String mockProd = context.getEnvironment().getProperty("com.binance.intra.mockprod.switch");
            if (!BooleanUtils.toBoolean(mockProd)) {
                return EnvUtil.isProd();
            } else {
                return true;
            }
        }

    }

    public static class GrayCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String envflag = EnvUtil.getEnvFlag();
            if (envflag != null && !StringUtils.equalsIgnoreCase(envflag, Constant.ENV_FLAG_NORMAL)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static class NormalCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String envflag = EnvUtil.getEnvFlag();
            if (envflag != null && !StringUtils.equalsIgnoreCase(envflag, Constant.ENV_FLAG_NORMAL)) {
                return false;
            } else {
                return true;
            }
        }

    }

    public static class Jdk11Condition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return EnvUtil.isJdk11();
        }
    }

    public static class DubboCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (isPresent("com.binance.platform.dubbo.DubboApplicationContextInitializer", context.getClassLoader())) {
                return true;
            }
            return false;
        }


        public static boolean isPresent(String className, ClassLoader classLoader) {
            if (classLoader == null) {
                classLoader = ClassUtils.getDefaultClassLoader();
            }
            try {
                forName(className, classLoader);
                return true;
            } catch (Throwable ex) {
                return false;
            }
        }

        private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
            if (classLoader != null) {
                return classLoader.loadClass(className);
            }
            return Class.forName(className);
        }
    }

}
