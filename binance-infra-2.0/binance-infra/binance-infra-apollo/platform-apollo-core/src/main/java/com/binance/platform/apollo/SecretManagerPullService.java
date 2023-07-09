package com.binance.platform.apollo;

import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;

public interface SecretManagerPullService extends Ordered {

    public void execut(PropertySource<?> secretManagerProperty);

}
