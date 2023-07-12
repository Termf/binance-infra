package com.binance.platform.env;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ConcurrentReferenceHashMap;

import com.binance.master.constant.Constant;
import com.google.common.collect.Sets;

public final class EnvUtil {

    protected static Environment env;

    private static final Map<String, String> CACHE_KEYPROPERTYS = new ConcurrentReferenceHashMap<String, String>(50);

    private static final Set<String> NEED_CACHE_KEYS = Sets.newConcurrentHashSet();

    static {
        ScheduledExecutorService scheduleReport = Executors.newScheduledThreadPool(1);
        scheduleReport.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!NEED_CACHE_KEYS.isEmpty() && env != null) {
                        for (String key : NEED_CACHE_KEYS) {
                            String value = env.getProperty(key);
                            if (value != null) {
                                CACHE_KEYPROPERTYS.put(key, value);
                            }
                        }
                    }
                } catch (Throwable e) {
                }
            }
        }, 0, 60, TimeUnit.SECONDS);

    }

    // binance默认需要告警应用
    private static final Set<String> ALARM_SERVICE =
        Sets.newHashSet(Arrays.asList("binance-mgs-common", "binance-mgs-account", "binance-mgs-capital",
            "binance-mgs-asset", "binance-mgs-lending", "binance-mgs-c2c", "binance-mgs-future", "account",
            "asset-service", "binance-margin-web", "lending-web", "mbx-gateway", "future-service", "sapi",
            "streamer-web", "streamer-open", "streamer-ordertrade", "streamer-userasset", "capital-web", "c2c"));

    public static void initCacheKeyFromEnvironment(String key) {
        String value = env.getProperty(key);
        if (value != null) {
            CACHE_KEYPROPERTYS.put(key, value);
        }
    }

    public static String getProperty(String key, String defaultValue) {
        boolean addSuccess = NEED_CACHE_KEYS.add(key);
        if (addSuccess) {
            initCacheKeyFromEnvironment(key);
        }
        return CACHE_KEYPROPERTYS.getOrDefault(key, defaultValue);
    }

    public static String getProperty(String key) {
        boolean addSuccess = NEED_CACHE_KEYS.add(key);
        if (addSuccess) {
            initCacheKeyFromEnvironment(key);
        }
        return CACHE_KEYPROPERTYS.get(key);
    }

    public static boolean isAlarmService(String serviceName) {
        String service = getProperty("management.monitor.alarm.service");
        if (service != null) {
            String[] newAlarmServiceAray = StringUtils.split(service, ",");
            List<String> newAlarmService = Arrays.asList(newAlarmServiceAray);
            boolean isProperSubCollection = CollectionUtils.isProperSubCollection(newAlarmService, ALARM_SERVICE);
            if (!isProperSubCollection) {
                ALARM_SERVICE.addAll(newAlarmService);
            }
        }
        return ALARM_SERVICE.contains(serviceName.trim().toLowerCase());
    }

    public static boolean isDocker() {
        File file = new File("/.dockerenv");
        if (file.exists()) {
            return true;
        } else {
            if (isK8s() || isEcs()) {
                return true;
            }
            return false;
        }
    }

    public static boolean isK8s() {
        String k8s = System.getenv("KUBERNETES_SERVICE_HOST");
        if (StringUtils.isNotBlank(k8s)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEcs() {
        String ecs = System.getenv("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI");
        if (StringUtils.isNotBlank(ecs)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isJdk11() {
        String jdkVersion = System.getProperty("java.version");
        return StringUtils.containsIgnoreCase(jdkVersion, "JDK11") || StringUtils.startsWith(jdkVersion, "11.0");
    }

    public static boolean isNotMacOs() {
        String OS = System.getProperty("os.name").toLowerCase();
        return !StringUtils.containsIgnoreCase(OS, "mac");
    }

    public static boolean isMacOs() {
        String OS = System.getProperty("os.name").toLowerCase();
        return StringUtils.containsIgnoreCase(OS, "mac os");
    }

    public static boolean isDev() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.equalsIgnoreCase(active, "dev") || StringUtils.endsWithIgnoreCase(active, "dev")
                    || StringUtils.equalsIgnoreCase(active, "local") || StringUtils.endsWithIgnoreCase(active, "local");
        }

        String env = System.getProperty("env", "dev");
        if (StringUtils.equalsIgnoreCase("dev", env) || StringUtils.endsWithIgnoreCase(env, "dev")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isQa() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.equalsIgnoreCase(active, "qa") || StringUtils.endsWithIgnoreCase(active, "qa");
        }
        String env = System.getProperty("env", "qa");
        if (StringUtils.equalsIgnoreCase("qa", env) || StringUtils.endsWithIgnoreCase(env, "qa")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isProd() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.equalsIgnoreCase(active, "prod") || StringUtils.endsWithIgnoreCase(active, "prod");
        }
        String env = System.getProperty("env", "prod");
        if (StringUtils.equalsIgnoreCase("prod", env) || StringUtils.endsWithIgnoreCase(env, "prod")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUSA() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.startsWithIgnoreCase(active, "usa");
        }
        String env = System.getProperty("env", "usa");
        if (StringUtils.startsWithIgnoreCase(env, "usa")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSGP() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.startsWithIgnoreCase(active, "sgp");
        }
        String env = System.getProperty("env", "sgp");
        if (StringUtils.startsWithIgnoreCase(env, "sgp")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isJE() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.startsWithIgnoreCase(active, "je");
        }
        String env = System.getProperty("env", "je");
        if (StringUtils.startsWithIgnoreCase(env, "je")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isUG() {
        if (env != null) {
            final String active = env.getProperty("spring.profiles.active", String.class);
            if (active != null)
                return StringUtils.startsWithIgnoreCase(active, "ug");
        }
        String env = System.getProperty("env", "ug");
        if (StringUtils.startsWithIgnoreCase(env, "ug")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前实例的灰度标签。如果是正常实例，则返回null.
     *
     * @return
     */
    public static String getEnvFlag() {
        String envFlag = null;
        if (env != null) {
            envFlag = env.getProperty("eureka.instance.metadataMap.envflag", String.class);
            if (envFlag == null) {
                envFlag = env.getProperty("spring.application.envflag", String.class);
            }
        }
        return Constant.ENV_FLAG_NORMAL.equals(envFlag) ? null : envFlag;
    }

    /**
     * Get the application deployFlag,
     * 
     * @return if no deployFlag found, fallback to envFlag
     */
    public static String getDeployFlag() {
        String deployFlag = null;
        if (env != null) {
            deployFlag = env.getProperty("eureka.instance.metadataMap.deployflag", String.class);
        }
        return StringUtils.defaultIfBlank(deployFlag, getEnvFlag());
    }

    public static void main(String[] args) {
        String OS = System.getProperty("os.name").toLowerCase();
        System.out.print(OS);
    }

}
