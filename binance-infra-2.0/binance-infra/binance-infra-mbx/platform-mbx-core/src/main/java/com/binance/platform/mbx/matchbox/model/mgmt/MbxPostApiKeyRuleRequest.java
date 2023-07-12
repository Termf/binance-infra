package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 2:01 下午
 */
public class MbxPostApiKeyRuleRequest extends MbxBaseRequest {
    private long keyId;
    private long accountId;
    @NotEmpty
    private String ip;

    public MbxPostApiKeyRuleRequest(long keyId, long accountId, String ip) {
        this.keyId = keyId;
        this.accountId = accountId;
        this.ip = ip;
    }

    @Override
    public String getUri() {
        return "/v1/apiKey/rule";
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
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
}
