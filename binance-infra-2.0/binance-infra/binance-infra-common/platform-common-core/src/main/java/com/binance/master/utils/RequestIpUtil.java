package com.binance.master.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.binance.master.constant.Constant;

public class RequestIpUtil {

	private static final Logger log = LoggerFactory.getLogger(RequestIpUtil.class);

	public static final String X_ORIGIN_FORWARD_FOR = "X-Origin-Forwarded-For";
	public static final String X_FORWARD_FOR = "X-Forwarded-For";
	public static final String UNKNOWN = "unknown";

	private static final String ANYHOST = "0.0.0.0";
	private static final String LOCALHOST = "127.0.0.1";

	private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
	private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
			Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	public static String getIpAddress(HttpServletRequest request) {
		if (WebUtils.isDubboRequest()) {
			return getIpAddressForDubbo();
		}
		String ipAddress = request.getHeader(X_ORIGIN_FORWARD_FOR);
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(X_FORWARD_FOR);
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (LOCALHOST.equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					log.error("unknown host error", e);
				}

			}
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			throw new IllegalArgumentException("获取不到客户端IP请检查网络配置");
		}
		return getRealIp(ipAddress, request);
	}

	public static String getIpAddressForDubbo() {
		String ipAddress = WebUtils.getDubboHeader(X_ORIGIN_FORWARD_FOR);
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader(X_FORWARD_FOR);
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader("HTTP_CLIENT_IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader(Constant.IP_ADDRESS);
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = WebUtils.getDubboHeader(Constant.IP_ADDRESS);
			if (LOCALHOST.equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					ipAddress = inet.getHostAddress();
				} catch (UnknownHostException e) {
					log.error("unknown host error", e);
				}

			}
		}
		if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
			throw new IllegalArgumentException("获取不到客户端IP请检查网络配置");
		}
		return getRealIpForDubbo(ipAddress);
	}

	private static String getRealIp(String ipAddress, HttpServletRequest request) {
		String realIp = "";
		if (ipAddress == null) {
			return realIp;
		}
		log.debug("ip addr: {},X-Origin-Forwarded-For={}", ipAddress, request.getHeader("X-Origin-Forwarded-For"));
		String[] ips = ipAddress.split(",");
		int ipIndex = 2;
		if (ips.length >= ipIndex) {
			realIp = ips[ips.length - ipIndex];
		} else {
			realIp = ips[0];
		}
		realIp = realIp.trim();
		if (!isIPv4Address(realIp) && !isIPv6StdAddress(realIp) && !isIPv6HexCompressedAddress(realIp)) {
			log.error("IP格式错误 {} {} ", ipAddress, realIp);
			realIp = "";
		}
		return realIp;
	}

	private static String getRealIpForDubbo(String ipAddress) {
		String realIp = "";
		if (ipAddress == null) {
			return realIp;
		}
		log.debug("ip addr: {},X-Origin-Forwarded-For={}", ipAddress, WebUtils.getDubboHeader("X-Origin-Forwarded-For"));
		String[] ips = ipAddress.split(",");
		int ipIndex = 2;
		if (ips.length >= ipIndex) {
			realIp = ips[ips.length - ipIndex];
		} else {
			realIp = ips[0];
		}
		realIp = realIp.trim();
		if (!isIPv4Address(realIp) && !isIPv6StdAddress(realIp) && !isIPv6HexCompressedAddress(realIp)) {
			log.error("IP格式错误 {} {} ", ipAddress, realIp);
			realIp = "";
		}
		return realIp;
	}

	public static boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6StdAddress(final String input) {
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6HexCompressedAddress(final String input) {
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	/**
	 * 手动获取ServletRequestAttributes
	 */
	public static ServletRequestAttributes getServletRequestAttributes() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra;
	}

	/**
	 * 把ServletRequestAttributes塞入到子线程
	 */
	public static void saveServletRequestAttributes(ServletRequestAttributes sra) {
		RequestContextHolder.setRequestAttributes(sra, true);
	}
}
