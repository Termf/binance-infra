//package com.binance.platform.openfeign.http2.okhttp;
//
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientConnectionPoolFactory;
//import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;
//import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
//import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
//import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
//import org.springframework.core.env.Environment;
//
//import com.google.common.collect.Lists;
//
//import okhttp3.ConnectionPool;
//import okhttp3.OkHttpClient;
//import okhttp3.Protocol;
//
//public class H2OkHttpClientFactory {
//
//    private static class SingletonHolder {
//        private static final H2OkHttpClientFactory instance = new H2OkHttpClientFactory();
//    }
//
//    private H2OkHttpClientFactory() {}
//
//    private okhttp3.OkHttpClient okHttpClient;
//
//    public static H2OkHttpClientFactory getInstance() {
//        return SingletonHolder.instance;
//    }
//
//    private OkHttpClientConnectionPoolFactory connPoolFactory() {
//        return new DefaultOkHttpClientConnectionPoolFactory();
//    }
//
//    private OkHttpClientFactory okHttpClientFactory() {
//        return new DefaultOkHttpClientFactory(new OkHttpClient.Builder());
//    }
//
//    private ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties) {
//        Integer maxTotalConnections = httpClientProperties.getMaxConnections();
//        Long timeToLive = httpClientProperties.getTimeToLive();
//        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
//        OkHttpClientConnectionPoolFactory connectionPoolFactory = this.connPoolFactory();
//        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
//    }
//
//    public okhttp3.OkHttpClient client(FeignHttpClientProperties httpClientProperties, Environment env) {
//        if (this.okHttpClient != null) {
//            return this.okHttpClient;
//        } else {
//            Boolean followRedirects = httpClientProperties.isFollowRedirects();
//            Integer connectTimeout = env.getProperty("ribbon.ConnectTimeout") != null
//                ? Integer.valueOf(env.getProperty("ribbon.ConnectTimeout"))
//                : httpClientProperties.getConnectionTimeout();
//            Integer readTimeout = Integer.valueOf(env.getProperty("ribbon.ReadTimeout"));
//            Boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
//            OkHttpClientFactory httpClientFactory = this.okHttpClientFactory();
//            ConnectionPool connectionPool = this.httpClientConnectionPool(httpClientProperties);
//            this.okHttpClient = httpClientFactory//
//                .createBuilder(disableSslValidation)//
//                .connectTimeout(connectTimeout * 2, TimeUnit.MILLISECONDS)//
//                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)//
//                .pingInterval(200, TimeUnit.MILLISECONDS)//
//                .followRedirects(followRedirects)//
//                .protocols(Lists.newArrayList(Protocol.H2_PRIOR_KNOWLEDGE))//
//                .connectionPool(connectionPool).build();
//            return this.okHttpClient;
//        }
//    }
//}
