package com.binance.platform.ribbon;

import java.util.Set;

public interface LbRetryConfigProvider {

    /**
     * 强制进行重试配置 methud+"#"+path,以这种方式来进行存放
     */
    Set<String> retryPathConfig();
}
