package com.binance.platform.openfeign;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ConfigurableApplicationContext;

import com.binance.platform.common.TrackingUtils;

import feign.Request;
import feign.Response;

/**
 * <pre>
 * 在极端情况下，ribbon.ReadTimeout/ribbon.ConnectTimeout会丢失掉，使用默认的10 * 1000, 60 * 1000替代掉
 * 
 * 这里进行强制的替换；
 * 好处：
 * 1: 能够统一控制
 * 
 * 坏处:
 * 1: 如果应用针对每一个单独的feignClient进行设置超时，不能够超过ribbon.ReadTimeout，否则将会被强制覆盖掉
 * 
 * </pre>
 * 
 */
public class ResetTimeoutFeignClient implements feign.Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetTimeoutFeignClient.class);

    private final feign.Client sourceClient;

    private final int ribbonConnectTimeout;

    private final int ribbonReadTimeout;

    public ResetTimeoutFeignClient(feign.Client sourceClient, ConfigurableApplicationContext context) {
        this.sourceClient = sourceClient;
        String myRibbonReadTimeout = context.getEnvironment().getProperty("ribbon.ReadTimeout", "3000");
        String myRibbonConnectTimeout = context.getEnvironment().getProperty("ribbon.ConnectTimeout", "3000");
        this.ribbonReadTimeout = Integer.valueOf(myRibbonReadTimeout);
        this.ribbonConnectTimeout = Integer.valueOf(myRibbonConnectTimeout);

    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        long start = System.currentTimeMillis();
        try {
            Request.Options resetOptions = this.resetOptions(options);
            return sourceClient.execute(request, resetOptions);
        } catch (SocketTimeoutException e) {
            long totalCost = System.currentTimeMillis() - start;
            MDC.put(TrackingUtils.REQUEST_TIME, String.valueOf(totalCost));
            LOGGER.error("API Call OpenFeign too Slow Request Error, uuid:{}, request:{} cost {} ms to complete",
                TrackingUtils.getTrace(), request.url(), "[" + totalCost + "]");
            throw e;
        }
    }

    /**
     * 重置feignHttpClient的超时时间，在极端情况下有可能会丢失，此时我们强制进行覆盖
     */

    private Request.Options resetOptions(Request.Options options) {
        int connectTimeout = options.connectTimeoutMillis();
        int readTimeout = options.readTimeoutMillis();
        boolean needReset = false;
        if (connectTimeout > this.ribbonConnectTimeout) {
            LOGGER.warn(
                "Ribbon options connectTimeoutMillis:{} is more than ribbon.ConnectTimeout {} and will force reset by ribbon.ConnectTimeout",
                connectTimeout, this.ribbonConnectTimeout);
            connectTimeout = this.ribbonConnectTimeout;
            needReset = true;
        }
        if (readTimeout > this.ribbonReadTimeout) {
            LOGGER.warn(
                "Ribbon options readTimeoutMillis:{} is more than ribbon.ReadTimeout {} and will force reset by ribbon.ReadTimeout",
                readTimeout, this.ribbonReadTimeout);
            readTimeout = this.ribbonReadTimeout;
            needReset = true;
        }
        if (needReset) {
            return new Request.Options(connectTimeout, readTimeout, options.isFollowRedirects());
        } else {
            return options;
        }

    }
}
