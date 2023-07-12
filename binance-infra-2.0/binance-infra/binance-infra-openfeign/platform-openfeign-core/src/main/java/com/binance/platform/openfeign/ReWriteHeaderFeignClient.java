package com.binance.platform.openfeign;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.util.UriComponentsBuilder;

import com.binance.platform.EurekaConstants;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.endpoint.ServiceDependenciesEndpoint;
import com.binance.platform.monitor.model.MicroServiceDepency;
import com.binance.platform.monitor.model.MicroServiceDepency.CallMicorService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import feign.Request;
import feign.Request.Body;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

/**
 * 扩展LoadBalancerFeignClient，再做负载均衡之前，可以重新设置一些header
 * 
 * 主要是为了幂等及加签，验签服务
 */
public class ReWriteHeaderFeignClient extends LoadBalancerFeignClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReWriteHeaderFeignClient.class);

    private static final AtomicReference<feign.Client> WRAPPED_LOADBALANCERFEIGNCLIENT_DELEGATE_REFERENCE =
        new AtomicReference<feign.Client>();

    private final LoadBalancerFeignClient client;

    private final ConfigurableApplicationContext context;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final String serviceName;

    private final MicroServiceDepency SERVICEDEPENCY = ServiceDependenciesEndpoint.SERVICEDEPENCY;

    private static feign.Client buildResetTimeoutFeignClient(LoadBalancerFeignClient client,
        ConfigurableApplicationContext context) {
        feign.Client wrappedDelegate = new ResetTimeoutFeignClient(client.getDelegate(), context);
        WRAPPED_LOADBALANCERFEIGNCLIENT_DELEGATE_REFERENCE.set(wrappedDelegate);
        return wrappedDelegate;
    }

    public ReWriteHeaderFeignClient(LoadBalancerFeignClient client, ConfigurableApplicationContext context) {
        super(buildResetTimeoutFeignClient(client, context),
            (CachingSpringLoadBalancerFactory)ReflectionByName(client, "lbClientFactory"),
            (SpringClientFactory)ReflectionByName(client, "clientFactory"));
        this.client = client;
        this.context = context;
        this.modifyLoadBalancerFeignClientDelegate(WRAPPED_LOADBALANCERFEIGNCLIENT_DELEGATE_REFERENCE.get());
        this.serviceName = context.getEnvironment().getProperty("spring.application.name");
        SERVICEDEPENCY.setServiceId(this.serviceName);
    }

    private void modifyLoadBalancerFeignClientDelegate(feign.Client newDelegateValue) {
        try {
            Field field = ReflectionUtils.findField(LoadBalancerFeignClient.class, "delegate");
            ReflectionUtils.makeAccessible(field);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            ReflectionUtils.makeAccessible(modifiersField);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            ReflectionUtils.setField(field, this.client, newDelegateValue);
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private static Object ReflectionByName(Object obj, String name) {
        Field field = ReflectionUtils.findField(obj.getClass(), name);
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, obj);
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {
        StopWatch timer = new StopWatch();
        Response response = null;
        try {
            timer.start();
            final Request sourceRequest = request;
            HttpMethod method = sourceRequest.httpMethod();
            String url = sourceRequest.url();
            Map<String, Collection<String>> headers = Maps.newHashMap(sourceRequest.headers());
            Body body = sourceRequest.requestBody();
            try {
                Map<String, ReWriteFeignClientHeaderHandler> reWriteFeignClientHeaderHandlers =
                    context.getBeansOfType(ReWriteFeignClientHeaderHandler.class);
                reWriteFeignClientHeaderHandlers.forEach((beanName, handler) -> {
                    handler.headers(sourceRequest, options, headers);
                });
            } catch (BeansException beansException) {
                // igore
            }
            // 如果有自定义的feignclientName的话，重新覆盖
            Collection<String> redefineFeignClientName = headers.get(EurekaConstants.REDEFINE_FEIGNCLIENT_NAME);
            if (redefineFeignClientName != null && redefineFeignClientName.size() != 0) {
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
                uriComponentsBuilder.host(redefineFeignClientName.iterator().next());
                url = uriComponentsBuilder.build().toUriString();
                headers.remove(EurekaConstants.REDEFINE_FEIGNCLIENT_NAME);
            }
            // 重新构造一个新的request
            request = Request.create(method, url, headers, body);
            response = this.client.execute(request, options);
            if (response.status() == 400) {
                String headerKeys = request.headers() != null ? request.headers().keySet().toString() : null;
                LOGGER.error("OpenFeign call return error,headers:{},request:{},response status:{}", headerKeys,
                    getRequestStr(request), response.status());
            }
            return response;
        } catch (Throwable t) {
            throw t;
        } finally {
            timer.stop();
            long totalCost = timer.getTotalTimeMillis();
            try {
                String requestUrlStr = getUrl(request.url());
                URL requestUrl = new URL(requestUrlStr);
                // 调用链
                CallMicorService callMicorService =
                    SERVICEDEPENCY.new CallMicorService(serviceName, requestUrl.getHost());
                SERVICEDEPENCY.addCall(callMicorService);
                // 埋点
                Timer micrometerTimer = Metrics.timer("openfeign",
                    Tags.of("status", null == response ? "None" : String.valueOf(response.status()), //
                        "method", request.httpMethod().name(), //
                        "uri", convertUrl(requestUrlStr, DEFAULT_RECURSIVE_COUNT)));
                micrometerTimer.record(totalCost, TimeUnit.MILLISECONDS);
                // 日志
                String total = EnvUtil.getProperty("management.monitor.totalcost", "2000");
                String alarmurls = EnvUtil.getProperty("management.monitor.client.ignore.alarmurl");
                if (totalCost >= Integer.valueOf(total)) {
                    MDC.put(TrackingUtils.REQUEST_TIME, String.valueOf(totalCost));
                    if (alarmurls != null) {
                        List<String> alarmUrlList = Lists.newArrayList(StringUtils.split(alarmurls, ","));
                        if (matchUrl(alarmUrlList, requestUrl.getPath())) {
                            LOGGER.warn(
                                "API Call OpenFegin too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                                TrackingUtils.getTrace(), request.url(), "[" + totalCost + "]");
                        } else {
                            LOGGER.error(
                                "API Call OpenFegin too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                                TrackingUtils.getTrace(), request.url(), "[" + totalCost + "]");
                        }
                    } else {
                        LOGGER.error(
                            "API Call OpenFegin too Slow Request Warning, uuid:{}, request:{} cost {} ms to complete",
                            TrackingUtils.getTrace(), request.url(), "[" + totalCost + "]");
                    }

                }
            } catch (Throwable t) {
                LOGGER.warn("record fail " + t.getMessage());
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

    /**
     * get the request string that with method and url, no sensitive info should be printed.
     * 
     * @param request
     * @return
     */
    private String getRequestStr(Request request) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.httpMethod()).append(' ').append(request.url()).append(" HTTP/1.1\n");
        return builder.toString();
    }

    @Override
    public feign.Client getDelegate() {
        return client.getDelegate();
    }

    /**
     * help method
     */
    private static final Pattern UUIDPATTERN =
        Pattern.compile("^[0-9a-f]{8}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{12}$");

    private static final Pattern BASE32PATTERN = Pattern.compile("^([A-Z2-7=]{8})+$");

    private static final Pattern URLPATTERN =
        Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");

    public static final int DEFAULT_RECURSIVE_COUNT = 3;

    private static String getUrl(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            url = url.substring(0, index);
        }
        return url;
    }

    private static String convertUrl(String url, int recursive) {
        if (recursive > 0) {
            String lastPart = StringUtils.substringAfterLast(url, "/");
            if (isAcronym(lastPart) || isNumeric(lastPart) || isUUID(lastPart) || isEncodedUrl(lastPart)) {
                return convertUrl(StringUtils.substringBeforeLast(url, "/"), recursive - 1);
            } else {
                return url;
            }
        } else {
            return url;
        }
    }

    private static boolean isAcronym(String word) {
        if (StringUtils.isEmpty(word))
            return false;
        // 如果是usdt、btc、bnb，也认为是币对，直接过滤掉
        if (StringUtils.equalsAnyIgnoreCase(word, "usdt", "btc", "bnb")) {
            return true;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str))
            return false;
        for (int i = str.length(); --i >= 0;) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    private static boolean isUUID(String str) {
        if (StringUtils.isEmpty(str))
            return false;
        return UUIDPATTERN.matcher(str).matches();
    }

    private static boolean isEncodedUrl(String str) {
        if (StringUtils.isEmpty(str))
            return false;
        if (BASE32PATTERN.matcher(str).matches()) {
            str = new String(new Base32().decode(str));
        }
        return URLPATTERN.matcher(str).matches();
    }

    public interface ReWriteFeignClientHeaderHandler {

        /**
         * 根据当前请求重新定义header
         */
        public void headers(final Request request, final Options options,
            final Map<String, Collection<String>> headers);
    }

}
