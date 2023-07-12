package com.binance.master.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

public final class IP2LocationUtils {

    private static final transient Logger logger = LogManager.getLogger(IP2LocationUtils.class);

    private static IP2Location ip2;

    public static void init(IP2Location ip2) {
        IP2LocationUtils.ip2 = ip2;
    }

    public static void setIP2Location(IP2Location ip2) {
        IP2LocationUtils.ip2 = ip2;
    }

    public static String getCountryCity(String ip) {
        if (ip2 == null) {
            return StringUtils.EMPTY;
        } else {
            try {
                IPResult rec = ip2.IPQuery(ip);
                if ("OK".equals(rec.getStatus())) {
                    String res = rec.getCity() + " " + rec.getCountryLong();
                    logger.info("ip:{},所在地{}", ip, res);
                    return res;
                } else {
                    return StringUtils.EMPTY;
                }
            } catch (Throwable e) {
                return StringUtils.EMPTY;
            }
        }
    }

    public static String getCountryShort(String ip) {
        if (ip2 == null) {
            return StringUtils.EMPTY;
        } else {
            try {
                IPResult rec = ip2.IPQuery(ip);
                if ("OK".equals(rec.getStatus())) {
                    String countryShort = rec.getCountryShort();
                    logger.info("ip:{}, 所在地简写:{}", ip, countryShort);
                    return countryShort;
                } else {
                    return StringUtils.EMPTY;
                }
            } catch (Throwable e) {
                return StringUtils.EMPTY;
            }
        }
    }

    public static IPResult getDetail(String ip) {
        if (ip2 == null) {
            return null;
        } else {
            try {
                IPResult rec = ip2.IPQuery(ip);
                return rec;
            } catch (Throwable e) {
                return null;
            }
        }
    }
}
