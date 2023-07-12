package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeleteApiKeyWhitelistRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/apiKey/whitelist";
    }
    private long keyId;
    private long accountId;

    public MbxDeleteApiKeyWhitelistRequest(long keyId, long accountId) {
        this.keyId = keyId;
        this.accountId = accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    public long getAccountId() {
        return accountId;
    }
}
