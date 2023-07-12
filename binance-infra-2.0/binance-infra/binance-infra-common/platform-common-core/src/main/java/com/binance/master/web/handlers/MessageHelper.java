package com.binance.master.web.handlers;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.master.error.ErrorCode;
import com.binance.master.utils.LanguageUtils;
import com.binance.master.utils.WebUtils;

public class MessageHelper {

    private static final Logger log = LoggerFactory.getLogger(MessageHelper.class);

    private final static String GENERAL_CODE_PACKAGE_NAME = "com.binance.master.error.GeneralCode";

    private final static String GENERAL_CODE_RESOURCE_FILE_NAME = "i18n/gcode_messages";

    private final static String MESSAGES_RESOURCE_FILE_NAME = "i18n/messages";

    private final static String ERROR_MESSAGES_RESOURCE_FILE_NAME = "i18n/error-messages";

    public String getMessage(ErrorCode errorCode, Object... params) {
        String key = errorCode.getClass().getName() + "." + errorCode;
        return doGetMessage(errorCode, key, null, params);
    }

    public String getMessage(ErrorCode errorCode, Locale locale, Object... params) {
        String key = errorCode.getClass().getName() + "." + errorCode;
        return doGetMessage(errorCode, key, locale, params);
    }

    public String getMessage(String key, Object... params) {
        return getMessage(key, null, params);
    }

    public String getMessage(HttpServletRequest request, ErrorCode errorCode, Object... params) {
        String key = errorCode.getClass().getName() + "." + errorCode;
        return doGetMessage(errorCode, request, key, null, params);
    }

    public String getMessage(HttpServletRequest request, String key, Object... params) {
        return getMessage(request, key, null, params);
    }

    private String language(HttpServletRequest request) {
        String language = LanguageUtils.getLanguage(request);
        String mappedlanguage = LanguageUtils.languageMapping(language);
        return mappedlanguage;
    }

    public String getMessage(HttpServletRequest request, String key, Locale locale, Object... params) {
        return doGetMessage(null, request, key, locale, params);
    }

    private String doGetMessage(ErrorCode errorCode, HttpServletRequest request, String key, Locale locale, Object... params) {
        // 国际化暂时只支持中英文，默认为英文
        if (locale == null) {
            locale = Locale.US;
            String language = language(request);
            if (StringUtils.equals("cn", language)) {
                locale = Locale.CHINA;
            }
        }
        String msg = getFormattedMsg(errorCode, key, locale, params, true);
        if (msg == null) {
            msg = getFormattedMsg(errorCode, key, Locale.US, params, false);
        }
        return msg;
    }

    public String getMessage(String key, Locale locale, Object... params) {
        return doGetMessage(null, key, locale, params);
    }

    private String doGetMessage(ErrorCode errorCode, String key, Locale locale, Object... params) {
        // 国际化暂时只支持中英文，默认为英文
        if (locale == null) {
            locale = Locale.US;
            String language = language(WebUtils.getHttpServletRequest());
            if (StringUtils.equals("cn", language)) {
                locale = Locale.CHINA;
            }
        }
        String msg = getFormattedMsg(errorCode, key, locale, params, true);
        if (msg == null) {
            msg = getFormattedMsg(errorCode, key, Locale.US, params, false);
        }
        return msg;
    }

    private String getFormattedMsg(ErrorCode errorCode, String key, Locale locale, Object[] params, boolean forceNullWhenMissing) {
        String msg = null;
        // 先从messages文件获取，找不到再从error-messages获取,同时支持两个文件获取的国际化
        try {
            msg = getValueFormResource(MESSAGES_RESOURCE_FILE_NAME, locale, key, params);
        } catch (MissingResourceException e) {
            try {
                msg = getValueFormResource(ERROR_MESSAGES_RESOURCE_FILE_NAME, locale, key, params);
            } catch (MissingResourceException ex) {
                msg = forceNullWhenMissing ? null : errorCode == null ? key : errorCode.getCode();
            }
        }
        return msg;
    }

    private String getValueFormResource(String resourceName, Locale locale, String key, Object[] params) throws MissingResourceException {
        String msg = null;
        try {
            String messagesBaseName = key.indexOf(GENERAL_CODE_PACKAGE_NAME) == 0 ? GENERAL_CODE_RESOURCE_FILE_NAME : resourceName;
            ResourceBundle messagesBundle = ResourceBundle.getBundle(messagesBaseName, locale);
            if (messagesBundle != null) {
                msg = messagesBundle.getString(key);
                if (StringUtils.isNotEmpty(msg) && params != null) {
                    msg = MessageFormat.format(msg, params);
                }
            }
        } catch (Throwable e) {
            String errorMsg = String.format("Can't find resource: %s bundle for key: %s", resourceName, key);
            log.warn(errorMsg);
            throw new MissingResourceException(errorMsg, resourceName, key);
        }
        return msg;
    }

}
