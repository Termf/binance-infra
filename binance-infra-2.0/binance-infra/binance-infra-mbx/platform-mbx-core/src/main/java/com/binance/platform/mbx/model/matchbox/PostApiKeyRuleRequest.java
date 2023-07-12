package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostApiKeyRuleRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private long keyId;
    @NotEmpty
    private String ip;

    public PostApiKeyRuleRequest(long accountId, String ip, long keyId) {
        this.accountId = accountId;
        this.ip = ip;
        this.keyId = keyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }
}