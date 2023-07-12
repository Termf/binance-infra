package com.binance.platform.resilience4j.ddos;

/**
 * the placeholder strategy with no customization. It makes an rate limiter created for every handler.
 *
 * @author Li Fenggang
 * Date: 2020/8/17 1:22 下午
 */
public final class SystemDefaultStrategy implements DdosStrategy {
    @Override
    public Boolean isDdos() {
        return null;
    }
}
