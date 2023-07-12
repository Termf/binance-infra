package com.binance.master.event;

import org.springframework.context.ApplicationEvent;

public class AwsKeyRefreshedEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 7767764645964203869L;
    private boolean intraSecurityOpened;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AwsKeyRefreshedEvent(Object source, boolean intraSecurityOpened) {
        super(source);
        this.intraSecurityOpened = intraSecurityOpened;
    }

    public boolean isIntraSecurityOpened() {
        return intraSecurityOpened;
    }
}
