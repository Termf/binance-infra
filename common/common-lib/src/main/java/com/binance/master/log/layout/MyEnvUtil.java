package com.binance.master.log.layout;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

public final class MyEnvUtil {

    protected static Environment env;

    public static boolean isDocker() {
        File file = new File("/.dockerenv");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
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
        String env = System.getProperty("env", "qa");
        if (StringUtils.equalsIgnoreCase("prod", env) || StringUtils.endsWithIgnoreCase(env, "prod")) {
            return true;
        } else {
            return false;
        }
    }
}
