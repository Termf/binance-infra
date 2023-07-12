package com.binance.platform.openfeign.http2.jetty;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.HttpDestination;
import org.eclipse.jetty.client.Origin;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.client.util.FutureResponseListener;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.SocketAddressResolver;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.util.thread.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import feign.Client;
import feign.Response;

public class JettyHttpClient implements Client {
    private static final Logger log = LoggerFactory.getLogger(JettyHttpClient.class);
    public static final int MAX_REQUEST_BODY_ON_UPGRADE = 1024 * 50;

    private HttpClient http1Client;
    private HttpClient http2Client;
    private final long http1IdleTimeout;
    private final long http1ConnectTimeout;
    private final int http1MaxConnectionsPerDestination;
    private final int http1MaxQueuedPerDestination;
    private final long http2ConnectionTimeout;
    private final long http2IdleTimeout;
    private final int http2MaxConnectionsPerDestination;
    private final int http2MaxRequestsQueuedPerDestination;
    private final int http2Selectors;
    private final int requestBufferSize;
    private final int maxResponseLength;
    private ConcurrentMap<Origin, HttpDestination> http2DestinationMap;

    public JettyHttpClient(long http1IdleTimeout, long http1ConnectTimeout, int http1MaxConnectionsPerDestination,
        int http1MaxQueuedPerDestination, long http2ConnectionTimeout, long http2IdleTimeout,
        int http2MaxConnectionsPerDestination, int http2MaxRequestsQueuedPerDestination, int http2Selectors,
        int requestBufferSize, int maxResponseLength) {
        this.http1IdleTimeout = http1IdleTimeout;
        this.http1ConnectTimeout = http1ConnectTimeout;
        this.http1MaxConnectionsPerDestination = http1MaxConnectionsPerDestination;
        this.http1MaxQueuedPerDestination = http1MaxQueuedPerDestination;
        this.http2ConnectionTimeout = http2ConnectionTimeout;
        this.http2IdleTimeout = http2IdleTimeout;
        this.http2MaxConnectionsPerDestination = http2MaxConnectionsPerDestination;
        this.http2MaxRequestsQueuedPerDestination = http2MaxRequestsQueuedPerDestination;
        this.http2Selectors = http2Selectors;
        this.requestBufferSize = requestBufferSize;
        this.maxResponseLength = maxResponseLength;
    }

    @PostConstruct
    public void init() throws Exception {
        initHttp1Client();
        initHttp2Client();
    }

    @PreDestroy
    public void destroy() {
        if (http1Client != null) {
            try {
                http1Client.stop();
            } catch (Exception e) {
                log.warn("stop http1Client failed", e);
            }
        }
        if (http2Client != null) {
            try {
                http2Client.stop();
            } catch (Exception e) {
                log.warn("stop http2Client failed", e);
            }
        }
    }

    private void initHttp1Client() throws Exception {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        http1Client = new HttpClient(sslContextFactory);
        http1Client.setConnectTimeout(http1ConnectTimeout);
        http1Client.setMaxConnectionsPerDestination(http1MaxConnectionsPerDestination);
        http1Client.setMaxRequestsQueuedPerDestination(http1MaxQueuedPerDestination);
        http1Client.setIdleTimeout(http1IdleTimeout);
        httpClientCommonConfig(http1Client);
        JettyHttpClientCustomizer.customize(http1Client);
        http1Client.start();
    }

    private void httpClientCommonConfig(HttpClient httpClient) throws Exception {
        // set max http header size
        httpClient.setRequestBufferSize(requestBufferSize);

        String name = "jetty-http@" + Integer.toHexString(hashCode());
        // 设置线程池
        QueuedThreadPool threadPool = new QueuedThreadPool(200, 10, 60000, new BlockingArrayQueue<>(128, 128));
        threadPool.setName(name);
        threadPool.start();
        // 设置ByteBufferPool
        ByteBufferPool byteBufferPool = new MappedByteBufferPool(2048, threadPool.getMaxThreads() / 2);
        // 设置Scheduler
        Scheduler scheduler = new ScheduledExecutorScheduler(name + "-scheduler", false);
        scheduler.start();

        httpClient.setTCPNoDelay(true);
        httpClient.setStrictEventOrdering(false);
        httpClient.setSocketAddressResolver(new SocketAddressResolver.Sync());
        httpClient.setExecutor(threadPool);
        httpClient.setByteBufferPool(byteBufferPool);
        httpClient.setScheduler(scheduler);

    }

    private void initHttp2Client() throws Exception {
        HTTP2Client http2LowLevelClient = new HTTP2Client();
        http2LowLevelClient.setInitialSessionRecvWindow(64 * 1024 * 1024);
        http2LowLevelClient.setInitialStreamRecvWindow(32 * 1024 * 1024);
        // disable dynamic header table
        http2LowLevelClient.setMaxDynamicTableSize(0);
        http2LowLevelClient
            .setSelectors(http2Selectors <= 0 ? Runtime.getRuntime().availableProcessors() : http2Selectors);
        HttpClientTransport transport = new HttpClientTransportOverHTTP2(http2LowLevelClient);

        http2Client = new HttpClient(transport);
        http2Client.setConnectTimeout(http2ConnectionTimeout);
        http2Client.setIdleTimeout(http2IdleTimeout);
        http2Client.setMaxConnectionsPerDestination(http2MaxConnectionsPerDestination);
        http2Client.setMaxRequestsQueuedPerDestination(http2MaxRequestsQueuedPerDestination);
        http2Client.setFollowRedirects(true);
        httpClientCommonConfig(http2Client);
        http2Client.setRemoveIdleDestinations(true);
        http2DestinationMap = JettyHttpClientCustomizer.customize(http2Client);
        http2Client.start();
    }

    private Request convertFeignRequest(feign.Request feignRequest, HttpClient httpClient,
        feign.Request.Options options) {
        Request httpRequest = httpClient.newRequest(feignRequest.url()).method(feignRequest.httpMethod().name());
        Map<String, Collection<String>> headers = feignRequest.headers();
        headers.forEach((header, values) -> {
            for (String value : values) {
                httpRequest.header(header, value);
            }
        });
        byte[] body = feignRequest.requestBody().asBytes();

        if (body != null) {
            String contentTypeKey = HttpHeaders.CONTENT_TYPE.toLowerCase();
            Collection<String> contentTypeList = feignRequest.headers().get(contentTypeKey);
            String contentType = MediaType.APPLICATION_JSON_VALUE;
            if (contentTypeList != null && contentTypeList.size() > 0) {
                contentType = contentTypeList.iterator().next();
            }
            ContentProvider content = new BytesContentProvider(contentType, body);
            httpRequest.content(content);
        }

        // options
        int connectTimeoutMillis = options.connectTimeoutMillis();
        int readTimeoutMillis = options.readTimeoutMillis();
        boolean followRedirects = options.isFollowRedirects();
        httpRequest.timeout(connectTimeoutMillis + readTimeoutMillis, TimeUnit.MILLISECONDS);
        httpRequest.idleTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS);
        httpRequest.followRedirects(followRedirects);

        return httpRequest;
    }

    private Request toJettyHttp1Request(feign.Request feignRequest, feign.Request.Options options) {
        Request http1Request = convertFeignRequest(feignRequest, http1Client, options);
        // add negotiate headers
        if (feignRequest.requestBody().length() < MAX_REQUEST_BODY_ON_UPGRADE) {
            http1Request.header(HttpHeader.UPGRADE, "h2c");
            http1Request.header(HttpHeader.HTTP2_SETTINGS, "");
            http1Request.header(HttpHeader.CONNECTION, "Upgrade, HTTP2-Settings");
        }
        return http1Request;
    }

    private Request toJettyHttp2Request(feign.Request feignRequest, feign.Request.Options options) {
        Request http2Request = convertFeignRequest(feignRequest, http2Client, options);
        return http2Request;
    }

    static feign.Response toFeignResponse(ContentResponse response, feign.Request feignRequest) {
        int status = response.getStatus();
        String reason = response.getReason();

        // headers
        HttpFields jettyHeaders = response.getHeaders();
        Map<String, Collection<String>> headers = new HashMap<>();
        for (HttpField header : jettyHeaders) {
            String headerName = header.getName();
            String[] values = header.getValues();
            if (values != null) {
                List<String> headerValues = Arrays.asList(values);
                headers.put(headerName, headerValues);
            } else {
                headers.put(headerName, Collections.emptyList());
            }
        }

        // body
        byte[] body = response.getContent();
        return Response.builder().status(status).reason(reason).headers(headers).request(feignRequest).body(body)
            .build();
    }

    @Override
    public feign.Response execute(feign.Request feignRequest, feign.Request.Options options) throws IOException {
        String url = feignRequest.url();
        URL urlObj = new URL(url);
        ContentResponse response = null;
        boolean isH2cServer = false;

        if (http2DestinationMap != null) {
            String host = urlObj.getHost();
            int port = urlObj.getPort();
            String protocol = urlObj.getProtocol();
            Origin origin = new Origin(protocol, host, port);
            if (http2DestinationMap.containsKey(origin)) {
                isH2cServer = true;
            }
        } else {
            log.warn("Jetty Http2 destination is not watched.");
        }

        if (!isH2cServer) {
            long startTime = System.currentTimeMillis();
            Request http1Request = toJettyHttp1Request(feignRequest, options);
            try {
                FutureResponseListener listener = buildFutureResponseListener(http1Request);
                http1Request.send(listener);
                response = listener.get(http1Request.getIdleTimeout(), TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                long costTime = System.currentTimeMillis() - startTime;
                processException("http1", http1Request, e, url, costTime);
            }
            if (response.getStatus() == 101) {
                isH2cServer = true;
            }
        } else {
            isH2cServer = true;
        }
        if (isH2cServer) {
            long startTime = System.currentTimeMillis();
            Request http2Request = toJettyHttp2Request(feignRequest, options);
            try {
                FutureResponseListener listener = buildFutureResponseListener(http2Request);
                http2Request.send(listener);
                response = listener.get(http2Request.getIdleTimeout(), TimeUnit.MILLISECONDS);
            } catch (Throwable x) {
                long costTime = System.currentTimeMillis() - startTime;
                processException("http2", http2Request, x, url, costTime);
            }
        }

        return toFeignResponse(response, feignRequest);
    }

    private RuntimeException processException(String protocol, Request request, Throwable x, String url, long costTime) throws SocketTimeoutException {
        TimeoutException timeoutException = null;
        if (x instanceof TimeoutException) {
            timeoutException = (TimeoutException) x;
        } else if (x.getCause() instanceof TimeoutException) {
            timeoutException = (TimeoutException)(x.getCause());
        }
        if (timeoutException != null) {
            request.abort(timeoutException);
            throw new SocketTimeoutException(
                    String.format("jetty %s request timeout, idleTimeout: %s, costTime: %s, url: %s",
                            protocol, request.getIdleTimeout(), costTime, url));
        }

        request.abort(x);
        throw new RuntimeException(
                String.format("jetty %s request failed(%s), url: %s", protocol, x.getMessage(), url), x);
    }

    private FutureResponseListener buildFutureResponseListener(Request request) {
        return new FutureResponseListener(request, maxResponseLength);
    }
}
