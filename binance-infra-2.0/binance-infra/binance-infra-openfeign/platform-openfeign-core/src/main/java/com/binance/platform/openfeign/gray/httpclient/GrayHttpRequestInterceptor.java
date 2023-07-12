package com.binance.platform.openfeign.gray.httpclient;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class GrayHttpRequestInterceptor implements HttpRequestInterceptor {
    public final HttpClientGrayHeaderCustomizer httpClientGrayHeaderCustomizer;

    public GrayHttpRequestInterceptor(HttpClientGrayHeaderCustomizer httpClientGrayHeaderCustomizer) {
        this.httpClientGrayHeaderCustomizer = httpClientGrayHeaderCustomizer;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        httpClientGrayHeaderCustomizer.apply(request);
    }
}
