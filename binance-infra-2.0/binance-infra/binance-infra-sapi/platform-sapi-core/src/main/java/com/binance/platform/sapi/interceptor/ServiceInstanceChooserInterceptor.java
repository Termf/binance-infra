package com.binance.platform.sapi.interceptor;

import com.binance.platform.sapi.annotation.Sapi;
import com.binance.platform.sapi.core.ServiceInstanceChooser;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;
import retrofit2.Invocation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * @author 陈添明
 */
public class ServiceInstanceChooserInterceptor implements Interceptor {

    private final ServiceInstanceChooser serviceInstanceChooser;

    public ServiceInstanceChooserInterceptor(ServiceInstanceChooser serviceDiscovery) {
        this.serviceInstanceChooser = serviceDiscovery;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Invocation invocation = request.tag(Invocation.class);
        assert invocation != null;
        Method method = invocation.method();
        Class<?> declaringClass = method.getDeclaringClass();
        Sapi sapi = declaringClass.getAnnotation(Sapi.class);
        String baseUrl = sapi.baseUrl();
        if (StringUtils.hasText(baseUrl)) {
            return chain.proceed(request);
        }
        // serviceId服务发现
        String serviceId = sapi.serviceId();
        URI uri = serviceInstanceChooser.choose(serviceId);
        HttpUrl url = request.url();
        HttpUrl newUrl = url.newBuilder()
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .build();
        Request newReq = request.newBuilder()
                .url(newUrl)
                .build();
        return chain.proceed(newReq);
    }
}
