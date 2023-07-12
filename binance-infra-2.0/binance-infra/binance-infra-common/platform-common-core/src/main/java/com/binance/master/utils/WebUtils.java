package com.binance.master.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.binance.master.constant.Constant;
import com.binance.master.enums.TerminalEnum;
import com.binance.master.models.APIRequest;
import com.binance.master.models.APIRequestHeader;
import com.binance.platform.common.RpcContext;

import eu.bitwalker.useragentutils.UserAgent;

@SuppressWarnings("deprecation")
public class WebUtils {
	private final static String UNKNOW_BROWSER = "UnKnownBrowser";
	private final static String UNKNOW_VERSION = "UnKnownVersion";
	private final static String UNKNOW_SYSTEM = "UnKnownSystem";
	public final static String LANG = "lang";
	private static final InheritableThreadLocal<HttpServletRequest> SERVERREQUEST_CACHE = new InheritableThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<Boolean> IS_DUBBO_REQUEST_CACHE = new ThreadLocal<Boolean>();

	public static boolean isDubboRequest() {
		Boolean flag = IS_DUBBO_REQUEST_CACHE.get();
		if (flag == null) {
			return false;
		} else {
			return flag;
		}
	}

	public static void cleanIsDubboRequest() {
		IS_DUBBO_REQUEST_CACHE.remove();
	}

	public static void setIsDubboRequest(boolean isDubbo) {
		IS_DUBBO_REQUEST_CACHE.set(isDubbo);
	}

	public static void cleanCurrentHttpRequest() {
		SERVERREQUEST_CACHE.remove();
	}

	public static void setCurrentHttpRequest(HttpServletRequest request) {
		SERVERREQUEST_CACHE.set(request);
	}

	private static ApplicationContext applicationContext;

	public static String getRequestIp() {
		if (WebUtils.isDubboRequest()) {
			return getDubboHeader(Constant.IP_ADDRESS);
		}
		HttpServletRequest request = getHttpServletRequest();
		String ip = getHeader(Constant.IP_ADDRESS);
		if (StringUtils.isBlank(ip)) {
			ip = RequestIpUtil.getIpAddress(request);
		}
		return ip;
	}

	public static String getHeader(String name) {
		if (WebUtils.isDubboRequest()) {
			return getDubboHeader(name);
		}
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		if (httpServletRequest == null) {
			return null;
		}
		return httpServletRequest.getHeader(name);
	}

	public static String getParameter(String name) {
		return getHttpServletRequest().getParameter(name);
	}

	public static HttpServletRequest getHttpServletRequest() {
		if (WebUtils.isDubboRequest()) {
			return null;
		}
		if (SERVERREQUEST_CACHE.get() != null) {
			HttpServletRequest request = SERVERREQUEST_CACHE.get();
			return request;
		} else {
			try {
				HttpServletRequest request = null;
				RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
				if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
					request = ((ServletRequestAttributes) requestAttributes).getRequest();
				}
				return request;
			} catch (Throwable e) {
				return null;
			}
		}
	}

	public static HttpServletResponse getHttpServletResponse() {
		if (WebUtils.isDubboRequest()) {
			return null;
		}
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		WebUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static APIRequestHeader getAPIRequestHeader() {// request 为空返回默认
		HttpServletRequest request = getHttpServletRequest();
		if (request == null) {
			return APIRequest.HEADER_DEFAULT;
		}
		return (APIRequestHeader) request.getAttribute(Constant.API_REQUEST_HEADER);
	}

	public static Map<String, Object> getDubboHeaders() {
		Map<String, Object> dubboHeaders = (Map<String, Object>) RpcContext.getContext().getAttachments().get("DUBBO-HEADERS");
		if (dubboHeaders == null) {
			dubboHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			RpcContext.getContext().getAttachments().put("DUBBO-HEADERS",dubboHeaders);
		}
		return dubboHeaders;
	}

	public static Iterator<String> getDubboHeaderNames() {
		return getDubboHeaders().keySet().iterator();
	}

	public static String getDubboHeader(String headerKey) {
		return (String) getDubboHeaders().get(headerKey);
	}

	public static Object getDubboObjectHeader(String headerKey) {
		return getDubboHeaders().get(headerKey);
	}

	public static void putDubboHeader(String headerKey, Object headerValue) {
		getDubboHeaders().put(headerKey, headerValue);
	}

	public static String getBrowser() {
		if (WebUtils.isDubboRequest()) {
			return getBrowserFromDubbo();
		}
		HttpServletRequest request = getHttpServletRequest();
		String browser = StringEscapeUtils.escapeHtml4(request.getHeader("User-Agent"));
		if (StringUtils.isBlank(browser)) {
			return "";
		} else if (browser.length() <= 120) {
			return browser;
		} else {
			return browser.substring(0, 120);
		}
	}

	public static String getOsAndBrowserInfo() {
		if (WebUtils.isDubboRequest()) {
			return getOsAndBrowserInfoFromDubbo();
		}
		String browserType = "";
		String browserVersion = "";
		String operation = "";
		HttpServletRequest request = getHttpServletRequest();
		if (getTerminal() != null && getTerminal().getCode().contains("pc")) {
			String pcParam = request.getParameter("pcParam");
			if (StringUtils.isBlank(pcParam)) {
				return "UnKnow Information";
			} else {
				return pcParam;
			}
		}
		if (getTerminal() != null && (getTerminal().getCode().equals("android") || getTerminal().getCode().equals("ios"))) {
			String os = request.getHeader("os");
			if (StringUtils.isBlank(os)) {
				os = request.getParameter("os");
				if (StringUtils.isBlank(os)) {
					return "UnKnow Information";
				} else {
					return os;
				}
			} else {
				return os;
			}
		}

		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		if (Objects.isNull(userAgent)) {
			return "UnKnow Information";
		}

		if (Objects.isNull(userAgent.getOperatingSystem()) || StringUtils.isBlank(userAgent.getOperatingSystem().getName())) {
			operation = UNKNOW_BROWSER;
		} else {
			operation = userAgent.getOperatingSystem().getName();
		}

		if (Objects.isNull(userAgent.getBrowserVersion()) || StringUtils.isBlank(userAgent.getBrowserVersion().getVersion())) {
			browserVersion = UNKNOW_VERSION;
		} else {
			browserVersion = userAgent.getBrowserVersion().getVersion();
		}

		if (Objects.isNull(userAgent.getBrowser()) || StringUtils.isBlank(userAgent.getBrowser().getName())) {
			browserType = UNKNOW_SYSTEM;
		} else {
			browserType = userAgent.getBrowser().getName();
		}

		return operation + " , " + browserType + " " + browserVersion;
	}

	public static TerminalEnum getTerminal() {
		if (WebUtils.isDubboRequest()) {
			return getTerminalFromDubbo();
		}
		HttpServletRequest request = getHttpServletRequest();
		String clientType = request.getParameter("clientType");
		if (StringUtils.isBlank(clientType)) {
			clientType = request.getHeader("clientType");
		}
		if (StringUtils.isBlank(clientType)) {
			// 保险参数
			if (request.getAttribute("clientTypeTemp") != null) {
				clientType = request.getAttribute("clientTypeTemp").toString();
			}
		}
		if (StringUtils.isNotBlank(clientType)) {
			clientType = clientType.toLowerCase();
		}
		TerminalEnum terminal = TerminalEnum.findByCode(clientType);
		return terminal;
	}


	public static String getBrowserFromDubbo() {
		String browser = StringEscapeUtils.escapeHtml4(getDubboHeader("User-Agent"));
		if (StringUtils.isBlank(browser)) {
			return "";
		} else if (browser.length() <= 120) {
			return browser;
		} else {
			return browser.substring(0, 120);
		}
	}

	public static String getOsAndBrowserInfoFromDubbo() {
		String browserType = "";
		String browserVersion = "";
		String operation = "";

		if (getTerminal() != null && (getTerminal().getCode().equals("android") || getTerminal().getCode().equals("ios"))) {
			return getDubboHeader("os");
		}

		UserAgent userAgent = UserAgent.parseUserAgentString(getDubboHeader("User-Agent"));
		if (Objects.isNull(userAgent)) {
			return "UnKnow Information";
		}

		if (Objects.isNull(userAgent.getOperatingSystem()) || StringUtils.isBlank(userAgent.getOperatingSystem().getName())) {
			operation = UNKNOW_BROWSER;
		} else {
			operation = userAgent.getOperatingSystem().getName();
		}

		if (Objects.isNull(userAgent.getBrowserVersion()) || StringUtils.isBlank(userAgent.getBrowserVersion().getVersion())) {
			browserVersion = UNKNOW_VERSION;
		} else {
			browserVersion = userAgent.getBrowserVersion().getVersion();
		}

		if (Objects.isNull(userAgent.getBrowser()) || StringUtils.isBlank(userAgent.getBrowser().getName())) {
			browserType = UNKNOW_SYSTEM;
		} else {
			browserType = userAgent.getBrowser().getName();
		}

		return operation + " , " + browserType + " " + browserVersion;
	}

	public static TerminalEnum getTerminalFromDubbo() {
		String clientType = null;
		if (StringUtils.isBlank(clientType)) {
			clientType = getDubboHeader("clientType");
		}
		if (StringUtils.isNotBlank(clientType)) {
			clientType = clientType.toLowerCase();
		}
		TerminalEnum terminal = TerminalEnum.findByCode(clientType);
		return terminal;
	}
}
