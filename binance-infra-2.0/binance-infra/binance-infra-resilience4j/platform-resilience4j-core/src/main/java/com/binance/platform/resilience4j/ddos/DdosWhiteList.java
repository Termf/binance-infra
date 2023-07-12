package com.binance.platform.resilience4j.ddos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/9/3
 */
public class DdosWhiteList {
    private static final Logger log = LoggerFactory.getLogger(DdosWhiteList.class);
    private List<String> whitelistPatterns;

    @Value("${com.binance.infra.ddos.ip-whitelist:}")
    public void setIpWhitelistPatterns(String ipWhiteList) {
        if (!StringUtils.hasText(ipWhiteList)) {
            whitelistPatterns = Collections.emptyList();
            log.info("ddos ip whitelist is empty");
        } else {
            String[] ipPatterns = StringUtils.tokenizeToStringArray(ipWhiteList, ",");
            List<String> newIpWhitelistPatterns = Arrays.asList(ipPatterns);

            whitelistPatterns = newIpWhitelistPatterns;
            log.info("ddos ip whitelist is :{}", whitelistPatterns);
        }
    }

    public boolean isInWhitelist(String ip) {
        if (ip == null) {
            log.debug("ddos whitelist ip: null");
            return true;
        }
        // 局域网IP
        if (ip.startsWith("10.")) {
            log.debug("ddos whitelist ip:[local] {}", ip);
            return true;
        }
        for (String whitelistPattern : whitelistPatterns) {
            if (ip.matches(whitelistPattern)) {
                log.debug("ddos whitelist ip: {}, whitelist pattern:{}", ip, whitelistPattern);
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        DdosWhiteList ddosWhiteList = new DdosWhiteList();

        String[] configs = new String[] {"", "47.243.35.119", "47.243.*", "47.243.35.119,8.210.222.169", "47.243.35.119,8.210.*"};
        for (String config : configs) {
            System.out.println("*** start " + config);
            ddosWhiteList.setIpWhitelistPatterns(config);
            doTemplate(ddosWhiteList, null);
            doTemplate(ddosWhiteList, "47.243.35.119");
            doTemplate(ddosWhiteList, "47.243.35.210");
            doTemplate(ddosWhiteList, "8.210.222.169");
            doTemplate(ddosWhiteList, "8.210.111.189");
            doTemplate(ddosWhiteList, "9.210.111.189");
        }
    }

    private static void doTemplate(DdosWhiteList ddosWhiteList, String ip) {
        System.out.println("ip: " + ip + "," + ddosWhiteList.isInWhitelist(ip));
    }
}
