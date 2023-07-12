package com.binance.platform.openfeign.body;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.binance.master.constant.Constant;
import com.binance.master.models.APIRequestHeader;
import com.binance.master.utils.LogMaskUtils;
import com.binance.platform.common.AlarmUtil;
import com.binance.platform.common.RpcContext;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.Monitors;
import com.binance.platform.monitor.logging.SampleUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class RequestBodyCacheFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestBodyCacheFilter.class);

    private static final String REQUEST_LOG_UUID_BODY =
        "uuid:{},request app:{},request Ip:{},request uri:{},requestBody:{}";
    private static final String REQUEST_LOG_UUID = "uuid:{},request app:{},request Ip:{},request uri:{}";
    private static final String REQUEST_LOG = "request uri:{},request app:{},request Ip:{}";
    private static final String REQUEST_LOG_BODY = "request uri:{},request app:{},request Ip:{},requestBody:{}";

    private static final List<String> REQUEST_LOG_EXCLUDE_URIS =
        Lists.newArrayList("/v2/api-docs/records", "/v2/api-docs/save-record");

    private final ObjectMapper objectMapper;

    protected final ConfigurableApplicationContext context;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final String appName;

    private boolean isProd = true;

    private static String SLOW_API_TEMPLATE = "慢API告警:【 %s 】API耗时:【 %sms 】UUID:【%s】 \n %s";

    public RequestBodyCacheFilter(ObjectMapper objectMapper, ConfigurableApplicationContext context) {
        this.objectMapper = objectMapper;
        this.context = context;
        String requestLogExcludeUrl = context.getEnvironment().getProperty("managent.logs.requestexcludeurl");
        this.appName = context.getEnvironment().getProperty("spring.application.name");
        this.isProd = EnvUtil.isProd() && !EnvUtil.isUSA();
        if (StringUtils.isNotEmpty(requestLogExcludeUrl)) {
            String[] urls = StringUtils.split(requestLogExcludeUrl, ",");
            REQUEST_LOG_EXCLUDE_URIS.addAll(Arrays.asList(urls));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), request.getMethod()) &&
            StringUtils.equals(request.getRequestURI(), "/")) {
            return true;
        }
        return super.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            HttpServletRequest servletRequest = (HttpServletRequest)request;
            Enumeration<String> headerNames = servletRequest.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerKey = headerNames.nextElement();
                    String headerValue = servletRequest.getHeader(headerKey);
                    if (headerKey.startsWith(RpcContext.HTTP_CUSTOMIZE_HEADER)
                        && !StringUtils.equalsIgnoreCase(TrackingUtils.TRACE_ID, headerKey)) {
                        RpcContext.getContext()
                            .set(StringUtils.replace(headerKey, RpcContext.HTTP_CUSTOMIZE_HEADER, ""), headerValue);
                    }
                }
            }
            if (!StringUtils.isEmpty(request.getContentType())
                && StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)
                && StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), request.getMethod())) {
                if (!(request instanceof CustomBodyServletRequestWrapper)
                    && !(response instanceof CustomServletResponseWrapper)) {
                    CustomBodyServletRequestWrapper customServletRequestWrapper =
                        new CustomBodyServletRequestWrapper(request);
                    CustomServletResponseWrapper customServletResponseWrapper =
                        new CustomServletResponseWrapper(response);
                    byte[] body = StringUtils.EMPTY.getBytes();
                    try {
                        // 如果要采样
                        boolean sample = SampleUtil.sampleIfAbsent(request);
                        // 重新设置采样
                        if (sample) {
                            customServletRequestWrapper.putHeader(SampleUtil.HTTP_HEADER_NAME,
                                SampleUtil.HTTP_HEADER_VALUE_SAMPLED);
                            SampleUtil.saveSampleTag(true);
                        } else {
                            customServletRequestWrapper.putHeader(SampleUtil.HTTP_HEADER_NAME,
                                SampleUtil.HTTP_HEADER_VALUE_NOT_SAMPLED);
                            SampleUtil.saveSampleTag(false);
                        }
                        body = customServletRequestWrapper.getBody();
                        APIRequestHeader apiRequestHeader = objectMapper.readValue(body, APIRequestHeader.class);
                        if (apiRequestHeader != null) {
                            customServletRequestWrapper.setAttribute("sys_header", apiRequestHeader);
                            customServletRequestWrapper.setAttribute(Constant.API_REQUEST_HEADER, apiRequestHeader);
                        }
                    } catch (Throwable e) {
                        // ignore the exception
                    }

                    String mask = "";
                    String enableRequestBodyLog = EnvUtil.getProperty("com.binance.infra.log.request.body.enable", "false");
                    if (Boolean.parseBoolean(enableRequestBodyLog)) {
                        mask = LogMaskUtils.fastMaskJsonStr(new String(body));
                    }
                    printRequestLog(customServletRequestWrapper, mask);
                    chain.doFilter(customServletRequestWrapper, customServletResponseWrapper);
                    customServletResponseWrapper.copyBodyToResponse();
                } else {
                    printRequestLog(request, null);
                    chain.doFilter(request, response);
                }
            } else {
                CustomeHeaderServletRequestWrapper customServletRequestWrapper = null;
                if (!(request instanceof CustomeHeaderServletRequestWrapper)) {
                    customServletRequestWrapper = new CustomeHeaderServletRequestWrapper(request);
                } else {
                    customServletRequestWrapper = (CustomeHeaderServletRequestWrapper)request;
                }
                CustomServletResponseWrapper customServletResponseWrapper = new CustomServletResponseWrapper(response);
                // 如果要采样
                boolean sample = SampleUtil.sampleIfAbsent(request);
                // 重新设置采样
                if (sample) {
                    customServletRequestWrapper.putHeader(SampleUtil.HTTP_HEADER_NAME,
                        SampleUtil.HTTP_HEADER_VALUE_SAMPLED);
                    SampleUtil.saveSampleTag(true);
                } else {
                    customServletRequestWrapper.putHeader(SampleUtil.HTTP_HEADER_NAME,
                        SampleUtil.HTTP_HEADER_VALUE_NOT_SAMPLED);
                    SampleUtil.saveSampleTag(false);
                }
                printRequestLog(customServletRequestWrapper, null);
                chain.doFilter(customServletRequestWrapper, customServletResponseWrapper);
                customServletResponseWrapper.copyBodyToResponse();
            }
        } finally {
            SampleUtil.clearSampleTag();
            long totalCost = System.currentTimeMillis() - start;
            String total = EnvUtil.getProperty("management.monitor.totalcost", "2000");
            String alarmurls = EnvUtil.getProperty("management.monitor.server.ignore.alarmurl");
            if (totalCost >= Integer.valueOf(total)) {
                MDC.put(TrackingUtils.REQUEST_TIME, String.valueOf(totalCost));
                if (alarmurls != null) {
                    List<String> alarmUrlList = Lists.newArrayList(StringUtils.split(alarmurls, ","));
                    if (this.matchUrl(alarmUrlList, request.getRequestURI())) {
                        logger.warn(
                            "API Call Server too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                            TrackingUtils.getTrace(), request.getRequestURI(), "[" + totalCost + "]");
                    } else {
                        logger.error(
                            "API Call Server too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                            TrackingUtils.getTrace(), request.getRequestURI(), "[" + totalCost + "]");
                    }
                } else {
                    if (isProd && EnvUtil.isAlarmService(this.appName) && totalCost >= 5000) {
                        AlarmUtil.alarmTeams(String.format(SLOW_API_TEMPLATE, this.appName, totalCost,
                            TrackingUtils.getTrace(), request.getRequestURI()));
                    }
                    logger.error("API Call Server too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                        TrackingUtils.getTrace(), request.getRequestURI(), "[" + totalCost + "]");
                }
            }

            if (logger.isDebugEnabled()) {
                String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
                String contentEncoding = response.getHeader(HttpHeaders.CONTENT_ENCODING);
                logger.info("##gzip check##,request uri:{}, Accept-Encoding:{}, Content-Encoding:{}",
                        request.getRequestURI(), acceptEncoding, contentEncoding);
            }
        }

    }

    private boolean matchUrl(List<String> alarmUrlList, String requestUrl) {
        if (CollectionUtils.containsAny(alarmUrlList, requestUrl)) {
            return true;
        }
        for (String alarmUrl : alarmUrlList) {
            boolean matcher = antPathMatcher.match(alarmUrl, requestUrl);
            if (matcher) {
                return true;
            }
        }
        return false;
    }

    private void printRequestLog(HttpServletRequest request, String requestBody) {
        if (REQUEST_LOG_EXCLUDE_URIS.contains(request.getRequestURI())) {
            return;
        }
        String uuid = request.getHeader(TrackingUtils.TRACE_ID_HEAD);
        String callApp = request.getHeader(TrackingUtils.TRACE_APP_HEAD) != null
            ? request.getHeader(TrackingUtils.TRACE_APP_HEAD) : "Unknown";
        String callIp = request.getRemoteAddr();
        Monitors.count("openfeign.client.app", "clientapp", callApp);
        if (StringUtils.isNotEmpty(uuid) && StringUtils.isNotBlank(requestBody)) {
            logger.info(REQUEST_LOG_UUID_BODY, uuid, callApp, callIp, request.getRequestURI(), requestBody);
        } else if (StringUtils.isNotEmpty(uuid) && StringUtils.isBlank(requestBody)) {
            logger.info(REQUEST_LOG_UUID, uuid, callApp, callIp, request.getRequestURI());
        } else if (StringUtils.isEmpty(uuid) && StringUtils.isNotBlank(requestBody)) {
            logger.info(REQUEST_LOG_BODY, request.getRequestURI(), callApp, callIp, requestBody);
        } else {
            logger.info(REQUEST_LOG, request.getRequestURI(), callApp, callIp);
        }
    }
}
