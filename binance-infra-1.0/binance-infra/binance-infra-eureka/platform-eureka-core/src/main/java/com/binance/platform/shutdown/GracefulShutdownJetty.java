package com.binance.platform.shutdown;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.Environment;

import com.netflix.discovery.EurekaClient;

public class GracefulShutdownJetty implements JettyServerCustomizer, GracefulShutdown, SmartApplicationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GracefulShutdownTomcat.class);

    private volatile Server server;

    private final int waitTime;

    private final EurekaClient eurekaClient;

    public GracefulShutdownJetty(Environment env, EurekaClient eurekaClient) {
        this.waitTime = getWaitTime(env);
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void customize(Server server) {
        this.server = server;
        LOGGER.info("Jetty Server customizer,wait time:{}", this.waitTime);
        StatisticsHandler handler = new StatisticsHandler();
        handler.setHandler(server.getHandler());
        server.setHandler(handler);
        server.setStopTimeout(this.waitTime);
        server.setStopAtShutdown(false);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent contextClosedEvent) {
        try {
            if (server == null) {
                return;
            }
            this.eurekaClient.shutdown();
            if (!(server.getHandler() instanceof StatisticsHandler)) {
                LOGGER.error("Root handler is not StatisticsHandler, graceful shutdown may not work at all!");
            } else {
                LOGGER.info("Active requests: " + ((StatisticsHandler)server.getHandler()).getRequestsActive());
            }
            long begin = System.currentTimeMillis();
            server.stop();
            LOGGER.info("Shutdown took " + (System.currentTimeMillis() - begin) + " ms.");
        } catch (Exception e) {
            LOGGER.error("Fail to shutdown gracefully.", e);
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
