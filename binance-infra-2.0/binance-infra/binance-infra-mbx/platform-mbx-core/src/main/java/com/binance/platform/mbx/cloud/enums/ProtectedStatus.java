package com.binance.platform.mbx.cloud.enums;

public enum ProtectedStatus {
    NORMAL_MODE,
    PROTECTED_MODE,
    FORBID_MODE;

    private ProtectedStatus() {
    }

    public boolean isInProtectedMode() {
        return this != NORMAL_MODE;
    }
}