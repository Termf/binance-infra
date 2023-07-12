package com.binance.platform.openfeign.gray.httpclient;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.context.ConfigurableApplicationContext;

import com.binance.platform.openfeign.gray.AbstractGrayHeaderCustomizer;

public class HttpClientGrayHeaderCustomizer extends AbstractGrayHeaderCustomizer<HttpRequest> {

    public HttpClientGrayHeaderCustomizer(ConfigurableApplicationContext context) {
        super(context);
    }

    @Override
    protected void addHeaderToRequest(HttpRequest request, String key, String value) {
        request.setHeader(key, value);
    }

    @Override
    protected boolean containsKey(HttpRequest request, String key) {
        return request.containsHeader(key);
    }

    @Override
    protected String getMethod(HttpRequest request) {
        if (request instanceof HttpRequestBase) {
            HttpRequestBase httpRequest = (HttpRequestBase)request;
            return httpRequest.getMethod();
        }
        return "";
    }

}
