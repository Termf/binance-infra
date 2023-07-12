package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class SaveApiRuleRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;
    private long keyId;
    @NotEmpty
    private String ip;

    public SaveApiRuleRequest(long userId, long keyId, @NotEmpty String ip) {
        this.userId = userId;
        this.keyId = keyId;
        this.ip = ip;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}