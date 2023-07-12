package com.binance.platform.openfeign.body;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.env.EnvUtil;

public class CustomeHeaderServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(CustomeHeaderServletRequestWrapper.class);
    /** true:encoded, false: not encoded */
    public static final String HEADER_ENCODED_HEADER_NAME = "x-header-encoded";

    private final Map<String, String> customHeaders;

    public static final Set<String> HEADERS_STANDARD_ON_REQUEST = new HashSet<>();

    static {
        Field[] fields = HttpHeaders.class.getFields();
        for (Field field : fields) {
            boolean publicStaticFinal = ReflectionUtils.isPublicStaticFinal(field);
            if (publicStaticFinal && field.getType().equals(String.class)) {
                try {
                    String headerName = ((String)field.get(null));
                    if (StringUtils.isNotEmpty(headerName)) {
                        HEADERS_STANDARD_ON_REQUEST.add(headerName.trim().toLowerCase());
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    public CustomeHeaderServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
        String host = request.getHeader(HttpHeaders.HOST);
        if (StringUtils.isBlank(host)) {
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String realHost = serverName + ":" + serverPort;
            customHeaders.put(HttpHeaders.HOST, realHost);
        }
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return decodeHeaderValue(name, ((HttpServletRequest)getRequest()).getHeader(name));
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return Collections.enumeration(Arrays.asList(headerValue));
        }

        Enumeration<String> sourceHeaders = ((HttpServletRequest)getRequest()).getHeaders(name);
        List<String> headerList = new ArrayList<>();
        while (sourceHeaders.hasMoreElements()) {
            String decodedHeaderValue = decodeHeaderValue(name, sourceHeaders.nextElement());
            headerList.add(decodedHeaderValue);
        }

        return Collections.enumeration(headerList);
    }

    private boolean isHeaderEncoded() {
        String value = ((HttpServletRequest)getRequest()).getHeader(HEADER_ENCODED_HEADER_NAME);
        return StringUtils.isNotBlank(value) && BooleanUtils.toBoolean(value);
    }

    private String decodeHeaderValue(String key, String value) {
        if (!HEADERS_STANDARD_ON_REQUEST.contains(key.toLowerCase())) {
            try {
                // 如果value被encode过，才进行decode
                String swithEncodeDecode = EnvUtil.getProperty("binance.header.encodedecode.enabled", "false");
                if (isHeaderEncoded() && BooleanUtils.toBoolean(swithEncodeDecode)) {
                    log.debug("decode for {}", key);
                    return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
                }
            } catch (Throwable e) {
                return value;
            }
        }
        return value;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> e = ((HttpServletRequest)getRequest()).getHeaderNames();
        while (e != null && e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }

    @Override
    public StringBuffer getRequestURL() {
        String host = super.getHeader(HttpHeaders.HOST);
        if (StringUtils.isNotBlank(host)) {
            StringBuffer url = new StringBuffer();
            String scheme = super.getScheme();
            url.append(scheme);
            url.append("://");
            url.append(host);
            url.append(super.getRequestURI());
            return url;
        } else {
            return super.getRequestURL();
        }

    }
}
