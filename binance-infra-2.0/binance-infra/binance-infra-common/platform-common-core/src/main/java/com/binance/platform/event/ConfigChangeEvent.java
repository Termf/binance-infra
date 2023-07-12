package com.binance.platform.event;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

public class ConfigChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8443286641779930893L;

    private Set<String> keys;

    public ConfigChangeEvent(Set<String> keys) {
        this(keys, keys);
    }

    public ConfigChangeEvent(Object context, Set<String> keys) {
        super(context);
        this.keys = keys;
    }

    /**
     * @return The keys.
     */
    public Set<String> getKeys() {
        return this.keys;
    }
}
