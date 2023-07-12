package com.binance.master.utils.version;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import com.binance.master.utils.WebUtils;

/**
 * 版本号小助手
 *
 * @author: caixinning
 * @date: 2018/05/18 15:44
 **/
public class VersionHelper {

    public static final String VERSION = "versionCode";
    public static final String VERSION_NAME = "versionName";

    /**
     *
     * @param srcVersionStr src版本
     * @param targetVersionStr target版本
     * @return 1：src版本高于target 0：版本相同 -1：版本落后
     */
    public static int compare(String srcVersionStr, String targetVersionStr) {
        ComparableVersion srcVersion = new ComparableVersion(srcVersionStr);
        ComparableVersion targetVersion = new ComparableVersion(targetVersionStr);

        return srcVersion.compareTo(targetVersion);
    }

    public static boolean higher(String srcVersionStr, String targetVersionStr) {
        return compare(srcVersionStr, targetVersionStr) == 1;
    }

    public static boolean higherOrEqual(String srcVersionStr, String targetVersionStr) {
        return compare(srcVersionStr, targetVersionStr) >= 0;
    }

    public static boolean lower(String srcVersionStr, String targetVersionStr) {
        return compare(srcVersionStr, targetVersionStr) == -1;
    }

    public static boolean equals(String srcVersionStr, String targetVersionStr) {
        return compare(srcVersionStr, targetVersionStr) == -1;
    }

    public static String getVersion(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getVersionFromDubbo();
        }
        if (Objects.isNull(request)) {
            return Strings.EMPTY;
        }
        String versionCode = request.getParameter(VERSION);
        if (StringUtils.isBlank(versionCode)) {
            versionCode = request.getHeader(VERSION);
        }
        return versionCode;
    }

    public static String getVersionName(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getVersionNameFromDubbo();
        }
        if (Objects.isNull(request)) {
            return Strings.EMPTY;
        }
        String versionName = request.getParameter(VERSION_NAME);
        if (StringUtils.isBlank(versionName)) {
            versionName = request.getHeader(VERSION_NAME);
        }
        return versionName;
    }

    public static boolean lowerVersionName(String compareVersionName, HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return lowerVersionNameFromDubbo(compareVersionName);
        }
        return lowerVersionName(compareVersionName, getVersionName(request));
    }


    public static String getVersionFromDubbo() {
        return WebUtils.getDubboHeader(VERSION);
    }

    public static String getVersionNameFromDubbo() {
        return WebUtils.getDubboHeader(VERSION_NAME);
    }

    public static boolean lowerVersionNameFromDubbo(String compareVersionName) {
        return lowerVersionName(compareVersionName, getVersionNameFromDubbo());
    }

    public static boolean lowerVersionName(String compareVersionName, String currentVersionName) {
        return compare(compareVersionName, currentVersionName) > 0;
    }

    public static void main(String[] args) {
        System.out.println(compare("1.4.7", "1.4.6"));
        System.out.println(compare("v1.4.7", "v1.4.6"));
        System.out.println(higherOrEqual("V 1.4.7", "v1.4."));
        System.out.println(higher("v1.4.7", "v1.4.6"));
        System.out.println(compare("57", "56"));

    }
}
