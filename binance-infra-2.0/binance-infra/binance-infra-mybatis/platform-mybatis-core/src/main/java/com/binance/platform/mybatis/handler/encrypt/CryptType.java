package com.binance.platform.mybatis.handler.encrypt;

public class CryptType {
    public static final CryptType create(String value) {
        return new CryptType(value);
    }

    private String value;

    private CryptType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EncryptedColumn{" + "value='" + value + '\'' + '}';
    }
}
