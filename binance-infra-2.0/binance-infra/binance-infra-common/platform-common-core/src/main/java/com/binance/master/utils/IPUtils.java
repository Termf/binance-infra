package com.binance.master.utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import com.binance.master.constant.Constant;

public class IPUtils {

    private static final Logger log = LoggerFactory.getLogger(IPUtils.class);

    public static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    public static final String X_ORIGIN_FORWARD_FOR = "X-Origin-Forwarded-For";
    public static final String X_FORWARD_FOR = "X-Forwarded-For";
    public static final String UNKNOWN = "unknown";

    private static final String ANYHOST = "0.0.0.0";
    private static final String LOCALHOST = "127.0.0.1";
    private static InetUtils inetUtils;

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private static synchronized InetUtils getInetUtils() {
        if (inetUtils == null) {
            final Map<String, String> envs = System.getenv();
            final Properties properties = new Properties();
            for (Map.Entry<String, String> env : envs.entrySet()) {
                if (StringUtils.startsWith(env.getKey(), InetUtilsProperties.PREFIX)) {
                    properties.put(env.getKey(), env.getValue());
                }
            }
            final Properties temp = System.getProperties();
            for (Map.Entry<Object, Object> enu : temp.entrySet()) {
                if (StringUtils.startsWith(enu.getKey().toString(), InetUtilsProperties.PREFIX)) {
                    properties.put(enu.getKey().toString(), enu.getValue());
                }
            }
            // System.out.println("InetUtils Params:" + JSON.toJSONString(properties));
            final MutablePropertySources propertySources = new MutablePropertySources();
            PropertiesPropertySource propertySource = new PropertiesPropertySource("sys-init", properties);
            propertySources.addLast(propertySource);

            final InetUtilsProperties target = new InetUtilsProperties();
            // springboot1.0
            // final RelaxedDataBinder binder = new RelaxedDataBinder(target, InetUtilsProperties.PREFIX);
            // binder.bind(new PropertySourcesPropertyValues(propertySources));

            // springboot2.0
            Binder newBinder = new Binder(new MapConfigurationPropertySource(properties));
            newBinder.bind(InetUtilsProperties.PREFIX, Bindable.ofInstance(target));

            inetUtils = new InetUtils(target);
        }
        return inetUtils;
    }

    private static volatile InetAddress LOCAL_ADDRESS = null;

    public static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return isValidAddress(name);
    }

    public static boolean isValidAddress(String address) {
        return (address != null && !ANYHOST.equals(address) && !LOCALHOST.equals(address) && IP_PATTERN.matcher(address).matches());
    }

    private static InetAddress getFirstValidAddress() {
        InetAddress result = null;
        InetUtils inetUtils = getInetUtils();
        if (inetUtils != null) {
            return inetUtils.findFirstNonLoopbackAddress();
        }
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics.hasMoreElements();) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    log.info("Testing interface: " + ifc.getDisplayName());
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else if (result != null) {
                        continue;
                    }
                    // @formatter:off
                    for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements();) {
                        InetAddress address = addrs.nextElement();
                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                            log.info("Found non-loopback interface: " + ifc.getDisplayName());
                            result = address;
                        }
                    }
                    // @formatter:on
                }
            }
        } catch (IOException ex) {
            log.error("Cannot get first non-loopback address", ex);
        }
        if (result != null) {
            return result;
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.warn("Unable to retrieve localhost");
        }
        return null;
    }

    public static InetAddress getAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getFirstValidAddress();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    public static String getIp() {
        InetAddress address = getAddress();
        if (address == null) {
            return null;
        }
        return address.getHostAddress();
    }

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

    public static String getAliIpAddress(HttpServletRequest request) {
        if (WebUtils.isDubboRequest()) {
            return getAliIpAddressForDubbo();
        }
        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isNotEmpty(ip) && ip.split(",").length > 0) {
            return ip.split(",")[0];
        }
        ip = request.getHeader(X_FORWARD_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getAliIpAddressForDubbo() {
        String ip = WebUtils.getDubboHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isNotEmpty(ip) && ip.split(",").length > 0) {
            return ip.split(",")[0];
        }
        ip = WebUtils.getDubboHeader(X_FORWARD_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.getDubboHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.getDubboHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.getDubboHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.getDubboHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = WebUtils.getDubboHeader(Constant.IP_ADDRESS);
        }
        return ip;
    }
}
