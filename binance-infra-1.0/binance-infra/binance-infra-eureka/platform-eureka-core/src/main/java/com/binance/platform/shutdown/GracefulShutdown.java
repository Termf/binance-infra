package com.binance.platform.shutdown;

import org.springframework.core.env.Environment;

public interface GracefulShutdown {

    public static final String GRACEFUL_WAIT_TIME = "server.gracefulwait";

    default int getWaitTime(Environment env) {
        return env.getProperty(GRACEFUL_WAIT_TIME, Integer.class, 60);
    }

}
