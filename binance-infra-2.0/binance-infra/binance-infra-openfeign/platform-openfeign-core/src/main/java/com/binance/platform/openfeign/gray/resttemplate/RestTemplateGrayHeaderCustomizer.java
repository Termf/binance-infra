//package com.binance.platform.openfeign.gray.resttemplate;
//
//import org.springframework.http.HttpRequest;
//
//import com.binance.platform.openfeign.gray.AbstractGrayHeaderCustomizer;
//
//public class RestTemplateGrayHeaderCustomizer extends AbstractGrayHeaderCustomizer<HttpRequest> {
//
//    public RestTemplateGrayHeaderCustomizer() {}
//
//    @Override
//    protected void addHeaderToRequest(HttpRequest request, String key, String value) {
//        request.getHeaders().set(key, value);
//    }
//
//    @Override
//    protected boolean containsKey(HttpRequest request, String key) {
//        return request.getHeaders().containsKey(key);
//    }
//
//    @Override
//    protected String getMethod(HttpRequest request) {
//        return request.getMethodValue();
//    }
//}
