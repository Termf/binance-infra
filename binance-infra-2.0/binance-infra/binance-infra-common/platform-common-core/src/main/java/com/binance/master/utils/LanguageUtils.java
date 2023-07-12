package com.binance.master.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.binance.master.constant.Constant;

public class LanguageUtils {

    public static String getLanguage(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getLanguageFromDubbo();
        }
        if (request != null) {
            String language = request.getHeader(Constant.LANG);
            if (StringUtils.isBlank(language)) {
                language = request.getParameter(Constant.LANG);
            }
            if (StringUtils.isBlank(language)) {
                language = getCookie(request, Constant.LANG);
            }
            if (StringUtils.isBlank(language) || StringUtils.equals(language, "undefined")) {
                language = "en";
            }
            return language;
        }
        return "en";
    }

    public static String getLanguageFromDubbo() {
        String language = WebUtils.getDubboHeader(Constant.LANG);
        if (StringUtils.isBlank(language) || StringUtils.equals(language, "undefined")) {
            language = "en";
        }
        return language;
    }

    /**
     * Notic: 大小写敏感
     *
     * cn -> zh-CN tw -> zh-TW ua -> uk-UA au -> en-AU es -> es-LA br -> pt-BR in -> en-IN ng -> en-NG
     *
     * @param sourceLanguage
     * @return
     */
    public static String languageMapping(String sourceLanguage) {
        if (StringUtils.isNotBlank(sourceLanguage)) {
            switch (sourceLanguage.toLowerCase()) {
                case "zh-cn":
                    return "cn";
                case "zh-tw":
                    return "tw";
                case "uk-ua":
                    return "ua";
                case "en-au":
                    return "au";
                case "es-es":
                    return "es";
                case "pt-br":
                    return "br";
                // case "en-in":
                // return "in";
                // case "en-ng":
                // return "ng";
                default:
                    return sourceLanguage;
            }
        }
        return sourceLanguage;

    }

    private static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static Locale getLocale(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getLocaleFromDubbo();
        }
        String localeStr = getLanguage(request);
        Locale locale = null;
        if (StringUtils.equalsIgnoreCase(localeStr, "cn")) {
            locale = Locale.CHINA;
        } else {
            locale = Locale.US;
        }
        return locale;
    }

    public static Locale getLocaleFromDubbo() {
        String localeStr = getLanguageFromDubbo();
        Locale locale = null;
        if (StringUtils.equalsIgnoreCase(localeStr, "cn")) {
            locale = Locale.CHINA;
        } else {
            locale = Locale.US;
        }
        return locale;
    }

    public static void main(String[] args) {
        String lan1 = languageMapping("zh-CN");
        String lan2 = languageMapping("zh-TW");
        String lan3 = languageMapping("uk-UA");
        String lan4 = languageMapping("en-AU");
        String lan5 = languageMapping("es-LA");
        String lan6 = languageMapping("pt-BR");

        System.out.println(lan1);
        System.out.println(lan2);
        System.out.println(lan3);
        System.out.println(lan4);
        System.out.println(lan5);
        System.out.println(lan6);

    }

}
