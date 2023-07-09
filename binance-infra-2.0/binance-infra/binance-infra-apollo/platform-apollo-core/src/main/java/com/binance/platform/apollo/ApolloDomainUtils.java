package com.binance.platform.apollo;

import java.util.Arrays;
import java.util.List;

import com.binance.platform.env.EnvUtil;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.google.common.collect.Lists;

public class ApolloDomainUtils {

    private static final String APOLLO_DOMAIN_NAMESPACE = "ops.domain";

    private static final String DEFAULT_DOMAIN = "https://www.binance.com/";

    private static final String DEV_DEFAULT_DOMAIN = "https://www.devfdg.net/";

    private static final String QA_DEFAULT_DOMAIN = "https://www.qa1fdg.net/";

    /**
     * 获取binance中国区官方域名
     * 
     * @return https://www.binancezh.cc/
     */
    public static final String getChinaDefaultDomain() {
        Config config = ConfigService.getConfig(APOLLO_DOMAIN_NAMESPACE);
        return config.getProperty("china.web.default.domain", DEFAULT_DOMAIN);
    }

    /**
     * 获取binance海外官方域名
     * 
     * @return https://www.binance.com/
     */
    public static final String getGlobalDefaultDomain() {
        if (EnvUtil.isDev()) {
            return DEV_DEFAULT_DOMAIN;
        } else if (EnvUtil.isQa()) {
            return QA_DEFAULT_DOMAIN;
        } else {
            return DEFAULT_DOMAIN;
        }
    }

    /**
     * 从ops.domain获取所想要的值
     * 
     * @return
     */
    public static final String getValueFromOpsDomain(String key) {
        Config config = ConfigService.getConfig(APOLLO_DOMAIN_NAMESPACE);
        return config.getProperty(key, null);
    }

    /**
     * 获取binance支持的顶级域名
     */
    public static final List<String> getGlobalSupportDomains() {
        Config config = ConfigService.getConfig(APOLLO_DOMAIN_NAMESPACE);
        String[] subDomains = config.getArrayProperty("whitelist.subdomain.all.suffix", ",", null);
        List<String> allDomains = Lists.newArrayList();
        if (subDomains != null) {
            allDomains.addAll(Arrays.asList(subDomains));
        }
        return allDomains;
    }

    /**
     * 获取binance支持的二级域名
     */
    public static final List<String> getGlobalSupportSubDomains() {
        Config config = ConfigService.getConfig(APOLLO_DOMAIN_NAMESPACE);
        String[] subDomains = config.getArrayProperty("whitelist.subdomain.all.prefix", ",", null);
        List<String> allDomains = Lists.newArrayList();
        if (subDomains != null) {
            allDomains.addAll(Arrays.asList(subDomains));
        }
        return allDomains;
    }

}
