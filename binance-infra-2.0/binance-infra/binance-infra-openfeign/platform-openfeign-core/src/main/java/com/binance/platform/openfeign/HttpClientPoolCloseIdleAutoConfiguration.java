package com.binance.platform.openfeign;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(name = "feign.httpclient.ApacheHttpClient")
@AutoConfigureAfter(FeignRibbonClientAutoConfiguration.class)
public class HttpClientPoolCloseIdleAutoConfiguration {

    @Autowired(required = false)
    private HttpClientConnectionManager connectionManager;

    @Autowired(required = false)
    private FeignHttpClientProperties httpClientProperties;

    private final Timer connectionManagerTimer =
        new Timer("FeignApacheHttpClientConfiguration.connectionManagerTimer", true);

    @PostConstruct
    public void construct() {
        if (connectionManager != null && httpClientProperties != null) {
            this.connectionManagerTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 在服务端keepalive、connectionTimeout的时间是60s，这个要比60s要短，直接清理50s无用的连接
                    connectionManager.closeIdleConnections(60 - 10, TimeUnit.SECONDS);
                }
            }, 30000, httpClientProperties.getConnectionTimerRepeat());
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        this.connectionManagerTimer.cancel();
    }

}
