package com.binance.platform.eureka.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import com.netflix.discovery.CacheRefreshedEvent;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.netflix.discovery.StatusChangeEvent;

public class EurekaClientEventListener implements EurekaEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaClientEventListener.class);

    private ApplicationEventPublisher publisher;

    public EurekaClientEventListener(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onEvent(EurekaEvent event) {
        if (event instanceof CacheRefreshedEvent) {
            publisher.publishEvent(new EurekaClientLocalCacheRefreshedEvent((CacheRefreshedEvent)event));
        } else if (event instanceof StatusChangeEvent) {
            StatusChangeEvent statusEvent = (StatusChangeEvent)event;
            LOGGER.info(String.format(" Application Status Changed from %s to %s",
                statusEvent.getPreviousStatus().name(), statusEvent.getStatus().name()));
            publisher.publishEvent(new EurekaClientStatusChangeEvent(statusEvent));
        }
    }

}
