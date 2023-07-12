package com.binance.platform.mbx.matchbox.rpc.impl;

import com.binance.master.error.GeneralCode;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.mbx.config.MbxCustomProperties;
import com.binance.platform.mbx.constant.MetricsConstants;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.matchbox.annotation.MbxField;
import com.binance.platform.mbx.matchbox.annotation.MbxIgnored;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import com.binance.platform.mbx.matchbox.rpc.MbxCaller;
import com.binance.platform.mbx.monitor.MonitorUtil;
import com.binance.platform.monitor.Monitors;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 4:46 下午
 */
public class OkHttp3MbxCaller implements MbxCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttp3MbxCaller.class);
    /** Cache the resolving result */
    private static final Map<Class, Map<String, Method>> REQUEST_CLASS_FIELD_MAP = new ConcurrentHashMap<>();
    /**
     * The name of the method used by request model to represent the request address
     */
    public static final String API_URI_METHOD = "getUri";
    public static final String MATCHBOX_ERROR_RESPONSE_PATTERN = "{\"code\":%s,\"msg\":\"%s\"}";
    @Autowired
    private MbxCustomProperties mbxCustomProperties;

    private OkHttpClient okHttpClient;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        long readTimeout = mbxCustomProperties.getReadTimeoutForMatchbox();
        int maxIdleConnections = mbxCustomProperties.getMaxIdleConnectionsForMatchbox();
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, 5, TimeUnit.MINUTES);
        builder
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectionPool(connectionPool);
        LOGGER.info("Matchbox client initialization with readTimeout({}s) and maxIdleConnections({})", readTimeout, maxIdleConnections);
        okHttpClient = builder.build();
    }

    @Override
    public String send(String rootUrl, HttpMethod httpMethod, MbxBaseRequest request, Map<String, List<String>> additionalParamMap) throws MbxException {
        // logic
        String uriString = buildNetRequestUri(rootUrl, request, additionalParamMap);
        Request okRequest = null;
        Map<String, String> headerMap = request.getHeaderMap();
        switch (httpMethod) {
            case GET:
                okRequest = buildGetRequest(uriString, headerMap);
                break;
            case POST:
                okRequest = buildPostRequest(uriString, request);
                break;
            case PUT:
                okRequest = buildPutRequest(uriString, headerMap);
                break;
            case DELETE:
                okRequest = buildDeleteRequest(uriString, headerMap);
                break;
            default:
                LOGGER.error("Unsupported http method:" + httpMethod);
                throw new MbxException(GeneralCode.SYS_ERROR);
        }
        long start = System.currentTimeMillis();
        Exception ex = null;
        String jsonResponse = null;
        Response response = null;
        Integer httpCode = null;
        String httpMessage = null;
        try {
            response = okHttpClient.newCall(okRequest).execute();
            httpCode = response.code();
            httpMessage = response.message();
            jsonResponse = response.body().string();
            boolean successful = response.isSuccessful();
            if (!successful && !StringUtils.hasText(jsonResponse)) {
                jsonResponse = String.format(MATCHBOX_ERROR_RESPONSE_PATTERN, httpCode, httpMessage);
            }
            return jsonResponse;
        } catch (IOException e) {
            ex = e;
            LOGGER.error(httpMethod + " " + request.getUri() + " error", e);
            throw new MbxException(GeneralCode.SYS_ERROR, e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            long elapseMs = System.currentTimeMillis() - start;
            if (elapseMs < NumberUtils.toInt(EnvUtil.getProperty("com.binance.infa.mbx.mbx-info-log-time", "3000"), 3000)) {
                LOGGER.info("{} {} takes [{} ms]. httpCode:{}, httpMessage:{}, response:{}",
                        httpMethod, uriString, elapseMs,
                        httpCode, httpMessage, jsonResponse);
            } else {
                LOGGER.error("{} {} takes [{} ms]. httpCode:{}, httpMessage:{}, response:{}",
                        httpMethod, uriString, elapseMs,
                        httpCode, httpMessage, jsonResponse);
            }
            Monitors.recordTime(MetricsConstants.METRICS_LATENCY_RPC_MBX, elapseMs, TimeUnit.MILLISECONDS,
                    MetricsConstants.TAG_NAME_ENTRANCE, MonitorUtil.entrance(),
                    MetricsConstants.TAG_NAME_MBX_URI, request.getUri(),
                    MetricsConstants.TAG_NAME_MBX_METHOD, httpMethod.toString(),
                    MetricsConstants.TAG_NAME_SPAN, "rpc.mbx",
                    MetricsConstants.TAG_NAME_HTTP_CODE, MonitorUtil.nonNullProcess(httpCode),
                    MetricsConstants.TAG_NAME_EXCEPTION, MonitorUtil.getExceptionTag(ex));
        }
    }

    private Request buildDeleteRequest(String uriString, Map<String, String> headerMap) {
        Request.Builder requestBuilder = new Request.Builder();
        Headers headers = Headers.of(headerMap);
        Request okRequest = requestBuilder.url(uriString)
                .delete(new FormBody.Builder().build())
                .headers(headers)
                .build();
        return okRequest;
    }

    private Request buildPutRequest(String uriString, Map<String, String> headerMap) {
        Request.Builder requestBuilder = new Request.Builder();
        Headers headers = Headers.of(headerMap);
        Request okRequest = requestBuilder.url(uriString)
                .put(new FormBody.Builder().build())
                .headers(headers)
                .build();
        return okRequest;
    }

    private Request buildPostRequest(String uriString, MbxBaseRequest request) {
        Request.Builder requestBuilder = new Request.Builder();
        // headers
        Map<String, String> headerMap = request.getHeaderMap();
        Headers headers = Headers.of(headerMap);
        // bodies
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        request.getFormData().entrySet().forEach(entry -> formBodyBuilder.add(entry.getKey(), entry.getValue()));
        Request okRequest = requestBuilder.url(uriString)
                .post(formBodyBuilder.build())
                .headers(headers)
                .build();
        return okRequest;
    }

    private Request buildGetRequest(String uriString, Map<String, String> headerMap) {
        Request.Builder requestBuilder = new Request.Builder();
        Headers headers = Headers.of(headerMap);
        Request okRequest = requestBuilder.url(uriString)
                .get()
                .headers(headers)
                .build();
        return okRequest;
    }

    /**
     * Compose uri
     *
     * @param rootUrl
     * @param request
     * @param additionalParamMap
     * @return
     * @throws MbxException
     */
    private String buildNetRequestUri(String rootUrl, MbxBaseRequest request, Map<String, List<String>> additionalParamMap) throws MbxException {
        String uri = request.getUri();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(rootUrl + uri);
        addUriParamFromRequest(request, uriComponentsBuilder);
        addUriParamFromAdditionalMap(additionalParamMap, uriComponentsBuilder);

        String uriString = uriComponentsBuilder.toUriString();
        return uriString;
    }

    /**
     * Add the additional params
     *
     * @param additionalParamMap
     * @param uriComponentsBuilder
     */
    private void addUriParamFromAdditionalMap(Map<String, List<String>> additionalParamMap, UriComponentsBuilder uriComponentsBuilder) {
        if (additionalParamMap != null) {
            Set<Map.Entry<String, List<String>>> entrySet = additionalParamMap.entrySet();
            for (Map.Entry<String, List<String>> stringListEntry : entrySet) {
                String key = stringListEntry.getKey();
                List<String> listEntryValue = stringListEntry.getValue();
                if (StringUtils.hasText(key) && listEntryValue != null && !listEntryValue.isEmpty()) {
                    String name = key.trim();
                    for (String value : listEntryValue) {
                        if (value != null) {
                            uriComponentsBuilder.queryParam(name, value);
                        }
                    }
                }
            }
        }
    }

    /**
     * Compose the params in the request.
     *
     * @param request
     * @param uriComponentsBuilder
     * @throws MbxException
     */
    private void addUriParamFromRequest(MbxBaseRequest request, UriComponentsBuilder uriComponentsBuilder) throws MbxException {
        Class<? extends MbxBaseRequest> requestClass = request.getClass();
        Map<String, Method> fieldMap = getFieldMap(requestClass);
        Set<Map.Entry<String, Method>> entrySet = fieldMap.entrySet();
        for (Map.Entry<String, Method> entry : entrySet) {
            String fieldName = entry.getKey();
            Method readMethod = entry.getValue();
            Object value = null;
            try {
                value = readMethod.invoke(request);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("Get request param error", e);
                throw new MbxException(GeneralCode.SYS_ERROR);
            }
            if (value == null) {
                LOGGER.debug("the value is null, ignored");
                continue;
            }
            if (value.getClass().isArray()) {
                // 数组
                Object[] array = (Object[]) value;
                for (Object object : array) {
                    String string = Objects.toString(object, null);
                    if (string != null) {
                        uriComponentsBuilder.queryParam(fieldName, string);
                    }
                }
            } else if (value instanceof Iterable) {
                // 集合
                Iterable iterable = (Iterable) value;
                Iterator iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    String string = Objects.toString(next, null);
                    if (string != null) {
                        uriComponentsBuilder.queryParam(fieldName, string);
                    }
                }
            } else {
                // primitive or String
                uriComponentsBuilder.queryParam(fieldName, value.toString());
            }
        }
    }

    private Map<String, Method> getFieldMap(Class<? extends MbxBaseRequest> requestClass) {
        Map<String, Method> fieldMap = REQUEST_CLASS_FIELD_MAP.get(requestClass);
        if (fieldMap == null) {
            synchronized (requestClass) {
                fieldMap = REQUEST_CLASS_FIELD_MAP.get(requestClass);
                if (fieldMap != null) {
                    return fieldMap;
                }

                fieldMap = resolveFieldMap(requestClass);
                REQUEST_CLASS_FIELD_MAP.put(requestClass, fieldMap);
            }
        }
        return fieldMap;
    }

    private Map<String, Method> resolveFieldMap(Class<? extends MbxBaseRequest> requestClass) {
        Map<String, Method> fieldMap = new HashMap<>(16);
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(requestClass, Object.class).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String fieldName = propertyDescriptor.getName();
                LOGGER.debug("field name is {}", fieldName);
                Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod == null) {
                    LOGGER.debug("read method is null");
                    continue;
                }

                String methodName = readMethod.getName();
                if (API_URI_METHOD.equals(methodName) || "getHeaderMap".equals(methodName)) {
                    LOGGER.debug("Is api address method");
                    continue;
                }

                MbxIgnored ignored = readMethod.getAnnotation(MbxIgnored.class);
                if (ignored != null) {
                    LOGGER.debug("this method is ignore by MbxIgnored");
                    continue;
                }

                MbxField mbxField = readMethod.getAnnotation(MbxField.class);
                if (mbxField != null) {
                    String customizedFieldName = mbxField.value();
                    if (StringUtils.hasText(customizedFieldName)) {
                        LOGGER.debug("field {} is replaced by {} due to MbxField", fieldName, customizedFieldName);
                        fieldName = customizedFieldName.trim();
                    }
                }

                fieldMap.put(fieldName, readMethod);
            }
        } catch (Exception e) {
            LOGGER.error("Logical error", e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }
        return fieldMap;
    }
}
