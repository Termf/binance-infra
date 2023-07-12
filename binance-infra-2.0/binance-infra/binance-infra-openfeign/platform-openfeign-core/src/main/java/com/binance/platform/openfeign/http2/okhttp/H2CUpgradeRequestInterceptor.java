//package com.binance.platform.openfeign.http2.okhttp;
//
//import java.io.IOException;
//import java.util.Queue;
//
//import org.apache.commons.collections4.queue.CircularFifoQueue;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
//import org.springframework.core.env.Environment;
//
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * 支持HTTP1.1和H2c协议的拦截
// * 
// */
//public class H2CUpgradeRequestInterceptor implements Interceptor {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(H2CUpgradeRequestInterceptor.class);
//
//    private final Queue<String> h2cServeQueue;
//
//    private final okhttp3.OkHttpClient h2cOkHttpClient;
//
//    public H2CUpgradeRequestInterceptor(FeignHttpClientProperties httpClientProperties, Environment env) {
//        this.h2cOkHttpClient = H2OkHttpClientFactory.getInstance().client(httpClientProperties, env);
//        this.h2cServeQueue = new CircularFifoQueue<String>(1000);
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Response response = null;
//        String destinationId = getDestinationId(request.url());
//        boolean isH2c = false;
//        if (h2cServeQueue.contains(destinationId)) {
//            LOGGER.debug("The previous H2c Request is cached and will use h2c direct, the remoteAddress is:{}",
//                destinationId);
//            isH2c = true;
//        } else {
//            Request h2cUpgradeRequest = request.newBuilder()//
//                .addHeader("Connection", "Upgrade, HTTP2-Settings")//
//                .addHeader("Upgrade", "h2c")//
//                .addHeader("HTTP2-Settings", StringUtils.EMPTY)//
//                .build();
//            response = chain.proceed(h2cUpgradeRequest);
//            if (response != null && response.code() == 101) {
//                LOGGER.debug("The Current Request Upgrade h2c Success, the remoteAddress is:{},will use h2c direct",
//                    destinationId);
//                h2cServeQueue.add(destinationId);
//                isH2c = true;
//            } else {
//                isH2c = false;
//            }
//        }
//        if (isH2c) {
//            return h2cOkHttpClient.newCall(request).execute();
//        } else {
//            return response;
//        }
//
//    }
//
//    private String getDestinationId(HttpUrl httpUrl) {
//        if (httpUrl == null || httpUrl.host() == null) {
//            return "UnknownHttpClient";
//        }
//        if (httpUrl.port() <= 0 || httpUrl.port() == HttpUrl.defaultPort(httpUrl.scheme())) {
//            return httpUrl.host();
//        }
//        final StringBuilder sb = new StringBuilder();
//        sb.append(httpUrl.host());
//        sb.append(':');
//        sb.append(httpUrl.port());
//        return sb.toString();
//    }
//
//}
