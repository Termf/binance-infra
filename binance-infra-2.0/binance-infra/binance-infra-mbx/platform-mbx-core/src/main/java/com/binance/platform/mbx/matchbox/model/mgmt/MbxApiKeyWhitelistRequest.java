package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxApiKeyWhitelistRequest extends MbxBaseRequest {

    private long keyId;
    private long accountId;

    public MbxApiKeyWhitelistRequest(long keyId, long accountId) {
        this.keyId = keyId;
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    @Override
    public String getUri() {
        return "/v3/apiKey/whitelist";
    }
}
