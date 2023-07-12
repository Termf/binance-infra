package com.binance.platform.openfeign.compress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * binanceframework
 *
 * @author Thomas Li
 * Date: 2021/1/15 10:25 下午
 */
public class GzipProperties implements EnvironmentAware {
    private static final Logger log = LoggerFactory.getLogger(GzipProperties.class);

    /**
     * Used to record the state of property feign.compression.response.enabled on starting.
     * Can not be changed in runtime. It means whether this service can receive the gzip response.
      */
    private boolean feignCompressionResponseEnabled;
    /**
     * Used to dynamically change the state whether allow the downstream return gzip response.
     * Though add Accepting-Encoding header to achieve the purpose.
     */
    private boolean acceptEncodingGzipEnabled;
    private boolean needGzipCompress;
    private boolean feignOkHttpEnabled;

    public boolean isFeignOkHttpEnabled() {
        return feignOkHttpEnabled;
    }

    @Value("${downstream.accept.encoding.zip.enabled:true}")
    private void setAcceptEncodingGzipEnabled(String enable) {
        boolean newEnable = Boolean.valueOf(enable);
        if (newEnable != acceptEncodingGzipEnabled) {
            log.info("accept encoding gzip enabled change to {} from {}", newEnable, acceptEncodingGzipEnabled);
        }
        acceptEncodingGzipEnabled = newEnable;
        refreshNeedGzipCompress();
    }

    public boolean isNeedGzipCompress() {
        return needGzipCompress;
    }

    private void refreshNeedGzipCompress() {
        boolean needGzipCompressCurrent = this.needGzipCompress;
        if (feignCompressionResponseEnabled && acceptEncodingGzipEnabled) {
            this.needGzipCompress = true;
        } else {
            this.needGzipCompress = false;
        }
        if (needGzipCompressCurrent != needGzipCompress) {
            log.info("needGzipCompress is changed from {} to {}", needGzipCompressCurrent, needGzipCompress);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String initialValue = environment.getProperty("feign.compression.response.enabled", "false");
        String feignOkHttpEnabledValue = environment.getProperty("feign.okhttp.enabled", "false");
        feignCompressionResponseEnabled = Boolean.valueOf(initialValue);
        feignOkHttpEnabled = Boolean.valueOf(feignOkHttpEnabledValue);
        refreshNeedGzipCompress();
    }
}
