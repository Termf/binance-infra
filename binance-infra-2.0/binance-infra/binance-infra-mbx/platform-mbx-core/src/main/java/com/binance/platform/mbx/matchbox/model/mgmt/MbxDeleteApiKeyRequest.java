package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeleteApiKeyRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/apiKey";
    }

    private long accountId;
    private long keyId;

    public MbxDeleteApiKeyRequest(long accountId, long keyId) {
        this.accountId = accountId;
        this.keyId = keyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }
}
