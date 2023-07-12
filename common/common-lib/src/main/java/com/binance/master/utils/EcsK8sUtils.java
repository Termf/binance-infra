package com.binance.master.utils;

import org.apache.commons.lang3.StringUtils;

public final class EcsK8sUtils {

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
}
