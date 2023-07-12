package com.binance.platform.openfeign.http2.jetty;

import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * binanceframework
 *
 * @author Thomas Li Date: 2021/4/2
 */
public class JettyHttpClientFactory {
    private JettyHttpClient jettyHttpClient;
    private long http1IdleTimeout;
    private long http1ConnectTimeout;
    private int http1MaxConnectionsPerDestination;
    private int http1MaxQueuedPerDestination;
    private long http2ConnectionTimeout;
    private long http2IdleTimeout;
    private int http2MaxConnectionsPerDestination;
    private int http2MaxRequestsQueuedPerDestination;
    private int http2Selectors;
    private int requestBufferSize;
    private int maxResponseLength;

    public JettyHttpClientFactory(FeignHttpClientProperties feignHttpClientProperties, Environment environment) {
        long httpIdleTimeout = Long.parseLong(environment.getProperty("feign.httpclient.idle-timeout", "50000"));
        int maxQueuedPerDestination =
            Integer.parseInt(environment.getProperty("feign.httpclient.max-queued-per-destination", "1024"));
        int http2Selectors = Integer.parseInt(environment.getProperty("feign.httpclient.http2-selectors",
            String.valueOf(Runtime.getRuntime().availableProcessors())));

        this.http1ConnectTimeout = feignHttpClientProperties.getConnectionTimeout();
        this.http1IdleTimeout = httpIdleTimeout;
        this.http1MaxConnectionsPerDestination = feignHttpClientProperties.getMaxConnectionsPerRoute();
        this.http1MaxQueuedPerDestination = maxQueuedPerDestination;
        this.http2ConnectionTimeout = feignHttpClientProperties.getConnectionTimeout();
        this.http2IdleTimeout = httpIdleTimeout;
        this.http2MaxConnectionsPerDestination = feignHttpClientProperties.getMaxConnectionsPerRoute();
        // 这个属性不需要调小，必要时可以调大，最终的值是在与服务端协商时，由服务端确定的
        this.http2MaxRequestsQueuedPerDestination = maxQueuedPerDestination;
        this.http2Selectors = http2Selectors;

        this.requestBufferSize = getDataSizeAsByte(environment, "server.max-http-header-size", "16KB");
        this.maxResponseLength = getDataSizeAsByte(environment, "jetty.client.response.max-length", "5MB");
    }

    private int getDataSizeAsByte(Environment environment, String key, String defaultValue) {
        String configValue = environment.getProperty(key, defaultValue);
        DataSize dataSize = DataSize.parse(configValue);
        return (int)dataSize.toBytes();
    }

    @PostConstruct
    public void init() throws Exception {
        this.jettyHttpClient =
            new JettyHttpClient(this.http1IdleTimeout, this.http1ConnectTimeout, this.http1MaxConnectionsPerDestination,
                this.http1MaxQueuedPerDestination, this.http2ConnectionTimeout, this.http2IdleTimeout,
                this.http2MaxConnectionsPerDestination, this.http2MaxRequestsQueuedPerDestination, this.http2Selectors,
                    this.requestBufferSize, this.maxResponseLength);
        this.jettyHttpClient.init();
    }

    @PreDestroy
    public void destroy() {
        this.jettyHttpClient.destroy();
    }

    public JettyHttpClient getJettyHttpClient() {
        return jettyHttpClient;
    }
}
