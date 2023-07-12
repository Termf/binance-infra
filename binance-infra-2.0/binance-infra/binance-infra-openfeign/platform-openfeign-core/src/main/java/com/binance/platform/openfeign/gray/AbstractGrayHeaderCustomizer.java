package com.binance.platform.openfeign.gray;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.binance.platform.openfeign.body.CustomeHeaderServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.encoding.HttpEncoding;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.binance.platform.common.RpcContext;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.openfeign.compress.GzipProperties;

public abstract class AbstractGrayHeaderCustomizer<R> implements GrayHeaderCustomizer<R> {
    public static final Logger logger = LoggerFactory.getLogger(AbstractGrayHeaderCustomizer.class);

    public static final Set<String> HEADERS_REMOVED_ON_REQUEST =
        new HashSet<>(Arrays.asList("connection", "keep-alive", "transfer-encoding", "te", "trailer",
            "proxy-authorization", "proxy-authenticate", "x-application-context", "upgrade", "content-length",
            TrackingUtils.TRACE_APP_HEAD.toLowerCase(), HttpHeaders.ACCEPT_ENCODING.toLowerCase(),
            HttpHeaders.COOKIE.toLowerCase(), HttpHeaders.USER_AGENT.toLowerCase(),
            CustomeHeaderServletRequestWrapper.HEADER_ENCODED_HEADER_NAME.toLowerCase()));

    private final ConfigurableApplicationContext context;

    private volatile Optional<GzipProperties> gzipPropertiesOptional;

    private Map<String, PenetrateHttpHeaderHandler> handlers;

    public AbstractGrayHeaderCustomizer(ConfigurableApplicationContext context) {
        this.context = context;
    }

    protected abstract void addHeaderToRequest(R request, String key, String value);

    private void addHeaderToRequestIntenal(R request, String key, String value) {
        if (!HEADERS_REMOVED_ON_REQUEST.contains(key.toLowerCase())) {
            // 只会在运行期间调用，所以不用担心bean被初始化多次的场景
            if (handlers == null) {
                this.handlers = context.getBeansOfType(PenetrateHttpHeaderHandler.class);
            }
            // 提供给应用扩展，是否需要透传header，如果需要则透传，并可以覆盖Header
            if (handlers != null && handlers.size() != 0) {
                handlers.forEach((beanName, handler) -> {
                    if (handler.needPenetrateHeader(key)) {
                        String replaceValue = handler.replaceHeader(key, value);
                        addHeaderToRequest(request, key, replaceValue);
                    }
                });
            }
            // 如果没有扩展类，则无脑透传
            else {
                addHeaderToRequest(request, key, value);
            }
        }
    }

    @Override
    public void apply(R request) {
        try {
            ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null && requestAttributes.getRequest() != null) {
                // add all header
                Enumeration<String> headerNames = requestAttributes.getRequest().getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String headerKey = headerNames.nextElement();
                        String headerValue = requestAttributes.getRequest().getHeader(headerKey);
                        if (headerValue == null) {
                            headerValue = StringUtils.EMPTY;
                        }
                        // 如果是key是Content-Type，并且是form表单提交，将Content-Type改成JSON
                        if (StringUtils.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE, headerKey)
                            && (StringUtils.containsIgnoreCase(headerValue, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                || StringUtils.containsIgnoreCase(headerValue, MediaType.MULTIPART_FORM_DATA_VALUE))
                            && StringUtils.equalsAnyIgnoreCase(getMethod(request), HttpMethod.POST.name(),
                                HttpMethod.PUT.name())) {
                            addHeaderToRequestIntenal(request, HttpHeaders.CONTENT_TYPE.toLowerCase(),
                                MediaType.APPLICATION_JSON_UTF8_VALUE);
                        } else {
                            addHeaderToRequestIntenal(request, headerKey, headerValue);
                        }
                        logger.debug(String.format("header key %s will transfer next service,and the value is %s",
                            headerKey, headerValue));
                    }
                }
            }
            this.addAcceptEncodingToRequest(request);
            // 透传RpcContext里所有的值
            Map<String, String> contextValues = RpcContext.getContext().get();
            if (contextValues != null && contextValues.size() != 0) {
                contextValues.forEach((k, v) -> {
                    String headerKey = RpcContext.HTTP_CUSTOMIZE_HEADER + k;
                    if (!containsKey(request, headerKey) && !StringUtils.equalsIgnoreCase(TrackingUtils.TRACE_ID, k)) {
                        addHeaderToRequestIntenal(request, headerKey, v);
                    }
                });
            }

        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    // 是否要添加Accept-Encoding
    private void addAcceptEncodingToRequest(R request) {
        Optional<GzipProperties> gzipPropertiesOptional = getGzipProperties();
        if (gzipPropertiesOptional.isPresent()) {
            GzipProperties gzipProperties = gzipPropertiesOptional.get();
            // OKHttp
            if (gzipProperties.isFeignOkHttpEnabled()) {
                if (gzipProperties.isNeedGzipCompress()) {
                    // OKHttp will add gzip header and decompress gzip response automatically.
                    // Can not add the ACCEPT_ENCODING header, otherwise it will not decompress gzip response.
                    // please refer to okhttp3.internal.http.BridgeInterceptor
                } else {
                    // prevent the okhttp default gzip logic
                    addHeaderToRequest(request, HttpHeaders.ACCEPT_ENCODING, HttpEncoding.DEFLATE_ENCODING);
                }
            } else {
                if (gzipProperties.isNeedGzipCompress()) {
                    addHeaderToRequest(request, HttpHeaders.ACCEPT_ENCODING, HttpEncoding.GZIP_ENCODING);
                }
            }
        }
    }

    protected abstract boolean containsKey(R request, String key);

    // 获取当前请求，不能获取外层的Http请求方式
    protected abstract String getMethod(R request);

    // help method
    private Optional<GzipProperties> getGzipProperties() {
        if (gzipPropertiesOptional == null) {
            Map<String, GzipProperties> interceptorMap = context.getBeansOfType(GzipProperties.class);
            for (GzipProperties properties : interceptorMap.values()) {
                gzipPropertiesOptional = Optional.of(properties);
                logger.info("initialize gzipPropertiesOptional with {}", properties);
                break;
            }
            if (gzipPropertiesOptional == null) {
                gzipPropertiesOptional = Optional.empty();
                logger.info("GzipProperties instance is not present");
            }
        }
        return gzipPropertiesOptional;
    }

}
