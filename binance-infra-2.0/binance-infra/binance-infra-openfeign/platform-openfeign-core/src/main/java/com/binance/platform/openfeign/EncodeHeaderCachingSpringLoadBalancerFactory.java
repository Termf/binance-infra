package com.binance.platform.openfeign;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ReflectionUtils;

import com.binance.platform.EurekaConstants;
import com.binance.platform.MyJsonUtil;
import com.binance.platform.ServerLoadStatus;
import com.binance.platform.env.EnvUtil;
import com.binance.platform.openfeign.body.CustomeHeaderServletRequestWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.client.ClientException;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import rx.Observable;

/**
 * 根据下游tomcat版本判断header头是否要进行encode
 * 
 * 如果全上了可以去除掉
 */
public class EncodeHeaderCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {
    private static final Logger log = LoggerFactory.getLogger(EncodeHeaderCachingSpringLoadBalancerFactory.class);

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

    public EncodeHeaderCachingSpringLoadBalancerFactory(
        CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory) {
        super((SpringClientFactory)getCachingSpringLoadBalancerFactoryValueByReflection(
            cachingSpringLoadBalancerFactory, "factory"));
    }

    public FeignLoadBalancer create(String clientName) {
        FeignLoadBalancer feignLoadBalancer = super.create(clientName);
        return new EncodeFeignLoadBalancer(feignLoadBalancer);
    }

    public static class EncodeFeignLoadBalancer extends FeignLoadBalancer {

        public EncodeFeignLoadBalancer(FeignLoadBalancer feignLoadBalancer) {
            super(feignLoadBalancer.getLoadBalancer(),
                (IClientConfig)getFeignLoadBalancerValueByReflection(feignLoadBalancer, "clientConfig"),
                (ServerIntrospector)getFeignLoadBalancerValueByReflection(feignLoadBalancer, "serverIntrospector"));
        }

        @Override
        public FeignLoadBalancer.RibbonResponse executeWithLoadBalancer(final FeignLoadBalancer.RibbonRequest request,
            final IClientConfig requestConfig) throws ClientException {
            LoadBalancerCommand<FeignLoadBalancer.RibbonResponse> command =
                buildLoadBalancerCommand(request, requestConfig);

            try {
                return command.submit(new ServerOperation<FeignLoadBalancer.RibbonResponse>() {
                    @Override
                    public Observable<FeignLoadBalancer.RibbonResponse> call(Server server) {
                        calculateEncodeHeader(request, server);
                        URI finalUri = reconstructURIWithServer(server, request.getUri());
                        FeignLoadBalancer.RibbonRequest requestForServer =
                            (FeignLoadBalancer.RibbonRequest)request.replaceUri(finalUri);
                        try {
                            return Observable.just(EncodeFeignLoadBalancer.this.execute(requestForServer, requestConfig));
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                }).toBlocking().single();
            } catch (Exception e) {
                Throwable t = e.getCause();
                if (t instanceof ClientException) {
                    throw (ClientException)t;
                } else {
                    throw new ClientException(e);
                }
            }

        }

        private void calculateEncodeHeader(FeignLoadBalancer.RibbonRequest request, Server server) {
            if (server instanceof DiscoveryEnabledServer) {
                DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer)server;
                String serverLoadJson = discoveryEnabledServer.getInstanceInfo().getMetadata()
                    .get(EurekaConstants.EUREKA_METADATA_SERVERLOAD);
                if (StringUtils.isNotBlank(serverLoadJson)) {
                    ServerLoadStatus serverLoadStatus = MyJsonUtil.fromJson(serverLoadJson, ServerLoadStatus.class);
                    String swithEncodeDecode = EnvUtil.getProperty("binance.header.encodedecode.enabled", "false");
                    if (serverLoadStatus != null
                        && StringUtils.equalsIgnoreCase("9.0.45", serverLoadStatus.getTomcatVersion())
                        && BooleanUtils.toBoolean(swithEncodeDecode)) {
                        log.debug("do Encode for some headers, tomcat version:{}", serverLoadStatus.getTomcatVersion());
                        Map<String, Collection<String>> headersSource = request.getRequest().headers();

                        // 如果开启了重试，那么第一次调用后，request中的header将被encode；第二次调用将再次encode，从而导致encode多次，因此这里需要避免多次encode
                        Collection<String> encodedHeaderValues = headersSource.get(CustomeHeaderServletRequestWrapper.HEADER_ENCODED_HEADER_NAME);
                        if (encodedHeaderValues != null && encodedHeaderValues.size() > 0 && encodedHeaderValues.contains("true")) {
                            log.warn("headers have been encoded, ignore encoding.");
                            return;
                        }

                        Map<String, Collection<String>> headersEncoded = Maps.newHashMap();
                        headersSource.forEach((k, v) -> {
                            headersEncoded.put(k, encodeHeaderValue(k, v));
                        });
                        headersSource.clear();
                        headersSource.putAll(headersEncoded);
                        headersSource.put(CustomeHeaderServletRequestWrapper.HEADER_ENCODED_HEADER_NAME,
                            Arrays.asList("true"));
                    }
                }
            }

        }

        private Collection<String> encodeHeaderValue(String key, Collection<String> value) {
            if (!HEADERS_STANDARD_ON_REQUEST.contains(key.toLowerCase())) {
                List<String> enCodeValues = Lists.newArrayList();
                for (Iterator<String> it = value.iterator(); it.hasNext();) {
                    String v = it.next();
                    log.debug("encode header for {}", key);
                    try {
                        enCodeValues.add(URLEncoder.encode(v, StandardCharsets.UTF_8.name()));
                    } catch (Throwable e) {
                        enCodeValues.add(v);
                    }
                }
                return enCodeValues;
            }
            return value;
        }

    }

    /**
     * help method
     */
    static Object getFeignLoadBalancerValueByReflection(FeignLoadBalancer feignLoadBalancer, String name) {
        try {
            Field field = ReflectionUtils.findField(FeignLoadBalancer.class, name);
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, feignLoadBalancer);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static Object getCachingSpringLoadBalancerFactoryValueByReflection(
        CachingSpringLoadBalancerFactory cachingSpringLoadBalancerFactory, String name) {
        try {
            Field field = ReflectionUtils.findField(CachingSpringLoadBalancerFactory.class, name);
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, cachingSpringLoadBalancerFactory);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
