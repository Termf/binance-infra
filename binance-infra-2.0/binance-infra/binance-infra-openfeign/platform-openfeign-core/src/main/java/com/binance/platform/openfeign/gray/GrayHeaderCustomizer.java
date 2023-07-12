package com.binance.platform.openfeign.gray;

public interface GrayHeaderCustomizer<R> {
    void apply(R request);
}
