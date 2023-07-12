package com.binance.platform.sapi.interceptor;

import com.binance.master.utils.WebUtils;
import com.binance.platform.common.RpcContext;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.monitor.Monitors;
import com.binance.platform.sapi.constant.Headers;
import com.binance.platform.sapi.config.BodyMetaData;
import com.binance.platform.sapi.config.MethodMetaData;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import retrofit2.Invocation;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * SAPI标准模块拦截器，支持的内容包括：<br/>
 * <ul>
 *     <li>1、自动签名（自动添加时间戳和时间窗口）</li>
 *     <li>2、传递上下文中的灰度标签（支持请求头和RPCContext）</li>
 *     <li>3、对于配置了内部调用源的客户端，自动添加调用源需要增加的内容</li>
 * </ul>
 *
 * @author Thomas Li
 * Date: 2021/7/21
 */
public class SapiInterceptor extends BaseGlobalInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SapiInterceptor.class);
    private static final ConcurrentMap<Method, MethodMetaData> metaMap = new ConcurrentHashMap<>();
    /** 灰度标签请求头 */
    public static final String GRAY_ENV_HEADER = "X-GRAY-ENV";
    /** SAPI请求源请求头 */
    public static final String HEADER_X_SAPI_SOURCE = "X-SAPI-SOURCE";
    /** SAPI路由请求头 */
    public static final String HEADER_X_SAPI_ROUTING = "X-SAPI-ROUTING";
    /** 路由到内部集群 */
    public static final String ROUTING_INTERNAL = "internal";
    public static final String HMAC_SHA_256 = "HmacSHA256";
    private static final String PARAM_NAME_TIMESTAMP = "timestamp";
    private static final String PARAM_NAME_RECV_WINDOW = "recvWindow";
    private static final String PARAM_NAME_SIGNATURE = "signature";
    public static final String METRIC_SIGNATURE = "sapi.client.signature";
    public static final String METRIC_REQUESTS = "sapi.client.requests";
    public static final String METRIC_NONE = "None";
    public static final String TAG_NAME_URI = "uri";
    public static final String TAG_NAME_EX = "ex";

    @Override
    protected Response doIntercept(Chain chain) throws IOException {
        long startMillisecond = System.currentTimeMillis();
        Request request = chain.request();
        Invocation invocation = request.tag(Invocation.class);
        Method method = invocation.method();
        MethodMetaData methodMetaData = getMetaData(method);

        Request newRequest = null;
        long signatureTimeCost = 0;
        String signatureError = METRIC_NONE;
        try {
            newRequest = buildTargetRequest(methodMetaData, request, invocation);
        } catch (Throwable t) {
            signatureError = t.getClass().getName();
            throw t;
        } finally {
            signatureTimeCost = System.currentTimeMillis() - startMillisecond;
            log.debug("signature cost: {}", signatureTimeCost);
            Monitors.recordTime(METRIC_SIGNATURE, signatureTimeCost, TimeUnit.MILLISECONDS,
                    TAG_NAME_URI, methodMetaData.getUri(),
                    TAG_NAME_EX, signatureError
                    );
        }

        String proceedError = METRIC_NONE;
        try {
            return chain.proceed(newRequest);
        } catch (Throwable t) {
            proceedError = t.getClass().getName();
            throw t;
        } finally {
            long timeCost = System.currentTimeMillis() - startMillisecond;
            log.debug("signature cost: {}, total cost {} ms", signatureTimeCost, timeCost);
            Monitors.recordTime(METRIC_REQUESTS, timeCost, TimeUnit.MILLISECONDS,
                    TAG_NAME_URI, methodMetaData.getUri(),
                    TAG_NAME_EX, proceedError);
        }
    }

    private Request buildTargetRequest(MethodMetaData methodMetaData, Request request, Invocation invocation) {

        Request.Builder newRequestBuilder = request.newBuilder();
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();

        // internal SAPI client source
        addRequestSource(newRequestBuilder);

        // gray header
        addGrayHeader(request, newRequestBuilder);

        boolean hasTimestamp = false;
        boolean hasRecWindow = false;

        // parse body
        BodyMetaData bodyMeta = methodMetaData.getBody();
        if (bodyMeta != null) {
            int paramIndex = bodyMeta.getParamIndex();
            Object bodyArg = invocation.arguments().get(paramIndex);
            if (bodyArg != null) {
                List<Field> fieldList = bodyMeta.getFieldList();
                for (Field field : fieldList) {
                    String fieldName = field.getName().trim();
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(bodyArg);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("No access", e);
                    }

                    if (fieldValue == null) {
                        continue;
                    }
                    String value = Objects.toString(fieldValue);
                    urlBuilder.addQueryParameter(fieldName, value);
                    if (PARAM_NAME_RECV_WINDOW.equals(fieldName)) {
                        hasRecWindow = true;
                    } else if (PARAM_NAME_TIMESTAMP.equals(fieldName)) {
                        hasTimestamp = true;
                    }
                }
                // remove request body
                newRequestBuilder.method(request.method(), new FormBody.Builder().build());
            }
        }

        // signature
        String secretKey = request.header(Headers.SECURITY_KEY);
        if (StringUtils.hasText(secretKey)) {
            newRequestBuilder.removeHeader(Headers.SECURITY_KEY);
            // need sign
            if (methodMetaData.isNeedSign()) {
                String apiKey = request.header(Headers.API_KEY);
                if (StringUtils.hasText(apiKey)) {
                    // timestamp
                    if (!hasTimestamp && httpUrl.queryParameter(PARAM_NAME_TIMESTAMP) == null) {
                        urlBuilder.addQueryParameter(PARAM_NAME_TIMESTAMP,
                                String.valueOf(System.currentTimeMillis()));
                    }
                    // recvWindow
                    if (!hasRecWindow && httpUrl.queryParameter(PARAM_NAME_RECV_WINDOW) == null) {
                        urlBuilder.addQueryParameter(PARAM_NAME_RECV_WINDOW, "5000");
                    }

                    String query = urlBuilder.build().query();
                    String signature = signByHmacSha256(secretKey, query);
                    urlBuilder.addQueryParameter(PARAM_NAME_SIGNATURE, signature);
                }
            }
        }
        // replace url
        newRequestBuilder.url(urlBuilder.build());
        return newRequestBuilder.build();
    }

    private void addRequestSource(Request.Builder newRequestBuilder) {
        String requestSource = EnvUtil.getProperty("sapi.sdk.request.source", "");
        if (StringUtils.hasText(requestSource)) {
            log.debug("add http header {}:{}", HEADER_X_SAPI_SOURCE, requestSource);
            newRequestBuilder.header(HEADER_X_SAPI_SOURCE, requestSource.trim());
        }

        String routingInternal = EnvUtil.getProperty("sapi.sdk.routing.internal", "true");
        if (Boolean.parseBoolean(routingInternal)) {
            log.debug("add http header {}:{}", HEADER_X_SAPI_ROUTING, ROUTING_INTERNAL);
            newRequestBuilder.header(HEADER_X_SAPI_ROUTING, ROUTING_INTERNAL);
        }
    }

    private void addGrayHeader(Request request, Request.Builder newRequestBuilder) {
        try {
            if (!StringUtils.hasText(request.header(GRAY_ENV_HEADER))) {
                String grayHeader = WebUtils.getHeader(GRAY_ENV_HEADER);
                if (StringUtils.hasText(grayHeader)) {
                    newRequestBuilder.header(GRAY_ENV_HEADER, grayHeader.trim());
                } else {
                    String envKey = RpcContext.getContext().get(GRAY_ENV_HEADER);
                    if (StringUtils.hasText(envKey)) {
                        newRequestBuilder.header(GRAY_ENV_HEADER, envKey.trim());
                    }
                }
            }
        } catch (Exception e) {
            log.error("add gray context error", e);
        }
    }

    private MethodMetaData getMetaData(Method method) {
        MethodMetaData methodMetaData = metaMap.get(method);
        if (methodMetaData == null) {
            methodMetaData = parseMethodMetaData(method);

            metaMap.putIfAbsent(method, methodMetaData);
        }
        return methodMetaData;
    }

    private MethodMetaData parseMethodMetaData(Method method) {
        MethodMetaData methodMetaData = new MethodMetaData();

        // parse params
        Parameter[] parameters = method.getParameters();
        boolean hasApiKey = false;
        boolean hasSecretKey = false;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Header header = parameter.getAnnotation(Header.class);
            if (header != null) {
                if (Headers.API_KEY.equalsIgnoreCase(header.value())) {
                    hasApiKey = true;
                }
                if (Headers.SECURITY_KEY.equalsIgnoreCase(header.value())) {
                    hasSecretKey = true;
                }
            }
            Body body = parameter.getAnnotation(Body.class);
            if (body != null) {
                methodMetaData.setBody(BodyMetaData.build(i, parameter.getType()));
            }
        }
        if (hasApiKey && hasSecretKey) {
            methodMetaData.setNeedSign(true);
        }

        // parse url
        Annotation[] methodAnnotations = method.getAnnotations();
        for (Annotation methodAnnotation : methodAnnotations) {
            if (methodAnnotation instanceof GET) {
                methodMetaData.setUri("GET@" + ((GET) methodAnnotation).value());
            } else if (methodAnnotation instanceof POST) {
                methodMetaData.setUri("POST@" + ((POST) methodAnnotation).value());
            } else if (methodAnnotation instanceof PUT) {
                methodMetaData.setUri("PUT@" + ((PUT) methodAnnotation).value());
            } else if (methodAnnotation instanceof DELETE) {
                methodMetaData.setUri("DELETE@" + ((DELETE) methodAnnotation).value());
            }
        }
        // other http method is not supported
        if (!StringUtils.hasText(methodMetaData.getUri())) {
            methodMetaData.setUri("Others");
        }

        return methodMetaData;
    }

    private String signByHmacSha256(String secretKey, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes("UTF-8"), HMAC_SHA_256);
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        }catch (Exception e) {
            log.error("SAPI HmacSha256 ERROR!",e);
            throw new RuntimeException(e);
        }
    }

}
