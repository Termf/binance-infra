package com.binance.feign.sample.provider.config;

public enum Color {

    RED(1), GREEN(2), BLANK(3), YELLO(4);

    private int value;

    private Color(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
