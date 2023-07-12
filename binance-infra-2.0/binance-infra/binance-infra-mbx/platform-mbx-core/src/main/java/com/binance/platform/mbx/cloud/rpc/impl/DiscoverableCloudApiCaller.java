package com.binance.platform.mbx.cloud.rpc.impl;

import com.binance.master.error.GeneralCode;
import com.binance.platform.EurekaConstants;
import com.binance.platform.common.RpcContext;
import com.binance.platform.common.TrackingUtils;
import com.binance.platform.mbx.cloud.constants.ApiConstants;
import com.binance.platform.mbx.cloud.rpc.CloudApiCaller;
import com.binance.platform.mbx.config.MbxCustomProperties;
import com.binance.platform.mbx.constant.LogConstants;
import com.binance.platform.mbx.exception.MbxException;
import com.binance.platform.mbx.monitor.MonitorUtil;
import com.binance.platform.mbx.util.JsonUtil;
import com.binance.platform.ribbon.HeaderGrayServerFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.appinfo.InstanceInfo;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Fenggang
 * Date: 2020/7/6 10:01 上午
 */
public class DiscoverableCloudApiCaller implements CloudApiCaller, EnvironmentAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoverableCloudApiCaller.class);
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /** warm up duration */
    private static final long WARM_UP_DURATION_IN_MILLISECOND = 60 * 1000;
    @Autowired
    private MbxCustomProperties mbxCustomProperties;
    private String applicationName;

    public DiscoverableCloudApiCaller(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private final DiscoveryClient discoveryClient;

    private OkHttpClient okHttpClient;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        long readTimeout = mbxCustomProperties.getReadTimeoutForInternalService();
        int maxIdleConnections = mbxCustomProperties.getMaxIdleConnectionsForInternalService();
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, 5, TimeUnit.MINUTES);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS)
                .connectionPool(connectionPool);
        LOGGER.info("Internal service client initialization with readTimeout({}s) and maxIdleConnections({})", readTimeout, maxIdleConnections);

        okHttpClient = builder.build();
    }

    public <T, U> U doCall(HttpMethod method, String serviceId, String path, Map<String, List<String>> queryParams, T request, TypeReference<U> typeReference) throws MbxException {
        Request serviceRequest = null;
        switch (method) {
            case GET:
                serviceRequest = buildGetRequest(method, serviceId, path, queryParams);
                break;
            case POST:
                serviceRequest = buildPostRequest(method, serviceId, path, request, queryParams);
                break;
            default:
                String message = "Http method " + method + " is not supported.";
                LOGGER.error(message);
                throw new MbxException(GeneralCode.SYS_NOT_SUPPORT, message);
        }
        Call call = okHttpClient.newCall(serviceRequest);
        Response response = null;
        String jsonResponse = null;
        long start = System.currentTimeMillis();
        Exception ex = null;
        try {
            response = call.execute();
            jsonResponse = response.body().string();
            LOGGER.debug("response:{}", jsonResponse);
        } catch (IOException e) {
            ex = e;
            LOGGER.error("Invoking service \"" + serviceId + "\" error", e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            long elapseMs = System.currentTimeMillis() - start;
            if (ApiConstants.loggable(serviceId)) {
                LOGGER.debug("service:{}, http.method:{}, uri:{}, takes [{} ms], ex:{}",
                        serviceId, method, path, elapseMs,
                        MonitorUtil.getExceptionTag(ex));
            }
        }
        U result = null;
        try {
            result = JsonUtil.fromJson(jsonResponse, typeReference);
        } catch (IOException e) {
            LOGGER.error("Deserialize json to Response error, json:" + jsonResponse, e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }
        return result;
    }

    private Request buildGetRequest(HttpMethod method, String serviceId, String path, Map<String, List<String>> queryParams) throws MbxException {
        String requestUri = buildRequestUri(method, serviceId, path, queryParams);
        Request.Builder builder = new Request.Builder();
        addCommonHeader(builder);
        Request request = builder.url(requestUri)
                .get().build();
        return request;
    }

    private void addCommonHeader(Request.Builder builder) {
        builder.header(TrackingUtils.TRACE_ID_HEAD, TrackingUtils.getTrace());
        builder.header(TrackingUtils.TRACE_APP_HEAD, this.applicationName);
    }

    private <T> Request buildPostRequest(HttpMethod method, String serviceId, String path, T request, Map<String, List<String>> queryParams) throws MbxException {
        String requestUri = buildRequestUri(method, serviceId, path, queryParams);
        String json = null;
        try {
            json = JsonUtil.toJsonString(request);
        } catch (JsonProcessingException e) {
            LOGGER.error("Serializing the request of \"" + serviceId + "\" error", e);
            throw new MbxException(GeneralCode.SYS_ERROR);
        }
        if (ApiConstants.loggable(serviceId)) {
            LOGGER.debug("post body:{}", json);
        }

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder builder = new Request.Builder();
        addCommonHeader(builder);
        return builder.url(requestUri)
                .post(requestBody)
                .build();
    }

    private String buildRequestUri(HttpMethod method, String serviceId, String path, Map<String, List<String>> requestParams) throws MbxException {
        String serviceHost = getServiceHost(serviceId);

        String apiUrl = serviceHost + path;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl);
        Set<Map.Entry<String, List<String>>> entries = requestParams.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> valueList = entry.getValue();
            if (StringUtils.hasText(key) && valueList != null && !valueList.isEmpty()) {
                for (String value : valueList) {
                    if (value != null) {
                        uriComponentsBuilder.queryParam(key, value);
                    }
                }
            }
        }
        String requestUri = uriComponentsBuilder.toUriString();

        if (ApiConstants.loggable(serviceId)) {
            LOGGER.debug("serviceId:{}, http.method:{}, path:{}, requestUrl:{}",
                    serviceId, method, path, requestUri);
        }
        return requestUri;
    }

    private String getServiceHost(String serviceId) throws MbxException {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        instances = filterInstances(instances);
        if (!instances.isEmpty()) {
            int pos = new Random().nextInt(instances.size());
            return instances.get(pos).getUri().toString();
        }

        LOGGER.error("No instance is found for service {} ", serviceId);
        throw new MbxException(GeneralCode.SYS_ERROR);
    }

    public List<ServiceInstance> filterInstances(List<ServiceInstance> instances) {
        String envTag = getEnvTagFromHeader();
        List<ServiceInstance> resultList = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            Map<String, String> metadata = instance.getMetadata();
            String instanceTag = metadata.get(EurekaConstants.EUREKA_METADATA_ENVKEY);

            if (Objects.equals(envTag, instanceTag)) {
                resultList.add(instance);
            } else if (envTag == null) {
                if (instanceTag == null
                        || instanceTag.isEmpty()
                        || EurekaConstants.EUREKA_META_ENVKEY_DEFAULT.equalsIgnoreCase(instanceTag)) {
                    resultList.add(instance);
                }
            }
        }
        if (resultList.isEmpty()) {
            return warmUp(instances);
        } else {
            return warmUp(resultList);
        }
    }

    public List<ServiceInstance> warmUp(List<ServiceInstance> serviceInstanceList) {
        List<ServiceInstance> warmUpList = new ArrayList<>();
        for (ServiceInstance serviceInstance : serviceInstanceList) {
            // Eureka instance
            if (serviceInstance instanceof EurekaDiscoveryClient.EurekaServiceInstance) {
                EurekaDiscoveryClient.EurekaServiceInstance eurekaServiceInstance =
                        (EurekaDiscoveryClient.EurekaServiceInstance)serviceInstance;
                InstanceInfo instanceInfo = eurekaServiceInstance.getInstanceInfo();
                Map<String, String> metadata = instanceInfo.getMetadata();
                boolean warmUp = Boolean.parseBoolean(
                        metadata.getOrDefault(EurekaConstants.EUREKA_METADATA_WARMUP, "true"));
                if (warmUp) {
                    long serviceUpTimestamp = ((InstanceInfo) instanceInfo).getLeaseInfo().getServiceUpTimestamp();
                    if (serviceUpTimestamp >= WARM_UP_DURATION_IN_MILLISECOND) {
                        warmUpList.add(serviceInstance);
                    }
                }

            }
        }
        if (warmUpList.isEmpty()) {
            return serviceInstanceList;
        }
        return warmUpList;
    }

    public String getEnvTagFromHeader() {
        String envTag = null;
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = requestAttributes.getRequest();
                envTag = request.getHeader(HeaderGrayServerFilter.GRAY_ENV_HEADER);
            }
            if (envTag == null) {
                envTag = RpcContext.getContext().get(HeaderGrayServerFilter.GRAY_ENV_HEADER);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        return envTag;
    }

    @Override
    public <T, U> U post(String serviceId, String path, T request, TypeReference<U> typeReference) throws MbxException {
        return post(serviceId, path, Collections.emptyMap(), request, typeReference);
    }

    @Override
    public <T, U> U post(String serviceId, String path, Map<String, List<String>> queryParams, T request, TypeReference<U> typeReference) throws MbxException {
        long start = System.currentTimeMillis();
        try {
            U u = doCall(HttpMethod.POST, serviceId, path, queryParams, request, typeReference);
            return u;
        } catch (MbxException e) {
            throw e;
        } finally {
            if (LOGGER.isDebugEnabled()) {
                long elapseMs = System.currentTimeMillis() - start;
                if (ApiConstants.loggable(serviceId)) {
                    LOGGER.debug("serviceId:{}, http.method:POST, path:{} takes [{} ms]",
                            serviceId, path, elapseMs);
                }
            }
        }
    }

    @Override
    public <T> T get(String serviceId, String path, TypeReference<T> typeReference) throws MbxException {
        return get(serviceId, path, Collections.emptyMap(), typeReference);
    }

    @Override
    public <T> T get(String serviceId, String path, Map<String, List<String>> queryParams, TypeReference<T> typeReference) throws MbxException {
        long start = System.currentTimeMillis();
        try {
            T t = doCall(HttpMethod.GET, serviceId, path, queryParams, null, typeReference);
            return t;
        } finally {
            if (LOGGER.isDebugEnabled()) {
                long elapseMs = System.currentTimeMillis() - start;
                if (ApiConstants.loggable(serviceId)) {
                    LOGGER.debug("serviceId:{}, http.method:GET, path:{} takes [{} ms]",
                            serviceId, path, elapseMs);
                }
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.applicationName = environment.getProperty("spring.application.name");
    }
}
