package com.binance.platform.openfeign.gray.feign;

import com.binance.platform.openfeign.gray.AbstractGrayHeaderCustomizer;
import feign.RequestTemplate;
import org.springframework.context.ConfigurableApplicationContext;

public class OpenFeignGrayHeaderCustomizer extends AbstractGrayHeaderCustomizer<RequestTemplate> {

    public OpenFeignGrayHeaderCustomizer(ConfigurableApplicationContext context) {
        super(context);
    }

    @Override
    public void addHeaderToRequest(RequestTemplate request, String key, String value) {
        request.header(key, value);
    }

    @Override
    public boolean containsKey(RequestTemplate request, String key) {
        return request.headers().containsKey(key);
    }

    @Override
    public String getMethod(RequestTemplate request) {
        return request.method();
    }

}
