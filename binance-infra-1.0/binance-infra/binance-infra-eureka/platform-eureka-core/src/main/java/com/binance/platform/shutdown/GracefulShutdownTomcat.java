package com.binance.platform.shutdown;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;

import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.EurekaClient;

public class GracefulShutdownTomcat implements TomcatConnectorCustomizer, GracefulShutdown, SmartApplicationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GracefulShutdownTomcat.class);

    private volatile Connector connector;

    private static final int CHECK_INTERVAL = 10;

    private final int waitTime;

    private final EurekaClient eurekaClient;

    public GracefulShutdownTomcat(Environment env, EurekaClient eurekaClient) {
        this.waitTime = getWaitTime(env);
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent contextClosedEvent) {
        LOGGER.info("Receive signal for graceful shutdown will unregister from eureka and stop tomcat in 120s");
        // 首先告诉eureka正在做优雅停机
        Map<String, String> metaData = this.eurekaClient.getApplicationInfoManager().getInfo().getMetadata();
        metaData.put("graceful", "true");
        this.eurekaClient.getApplicationInfoManager().registerAppMetadata(metaData);
        // eureka状态置为down
        this.eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceStatus.DOWN);
        // 停10s，让运维在eureka控制台看到有down的过程
        this.sleep(10);
        // 主动从eureka下线，从eureka上摘除本节点
        this.eurekaClient.shutdown();
        // 停30s，这是为了防止调用方没去刷新本地的IP列表
        this.sleep(30);
        // 把tomcat的连接断掉，不再接收新的请求
        this.connector.pause();
        // 把tomcat积攒的任务全部消费掉，需要在60s内全部处理完毕
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executor;
                threadPoolExecutor.shutdown();
                for (long remaining = waitTime; remaining > 0; remaining -= CHECK_INTERVAL) {
                    LOGGER.info("{} thread(s) active, {} seconds remaining", threadPoolExecutor.getActiveCount(),
                        remaining);
                    if (threadPoolExecutor.awaitTermination(CHECK_INTERVAL, TimeUnit.SECONDS)) {
                        return;
                    }
                }
            } catch (InterruptedException ex) {
                LOGGER.error("interrupted error", ex);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sleep(int sleepTime) {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException ex) {
            LOGGER.error("interrupted error", ex);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ContextClosedEvent.class.isAssignableFrom(eventType);

    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

}
