package com.binance.platform.openfeign.compress;

import feign.RequestTemplate;
import org.springframework.cloud.openfeign.encoding.FeignAcceptGzipEncodingInterceptor;
import org.springframework.cloud.openfeign.encoding.FeignClientEncodingProperties;

/**
 * binance-infra-whole
 *
 * @author Thomas Li
 * Date: 2021/1/13 2:23 下午
 */
public class GzipFeignAcceptGzipEncodingInterceptor extends FeignAcceptGzipEncodingInterceptor {

    /**
     * Creates new instance of {@link FeignAcceptGzipEncodingInterceptor}.
     *
     * @param properties the encoding properties
     */
    public GzipFeignAcceptGzipEncodingInterceptor(FeignClientEncodingProperties properties) {
        super(properties);
    }

    @Override
    public void apply(RequestTemplate template) {
    }

}
