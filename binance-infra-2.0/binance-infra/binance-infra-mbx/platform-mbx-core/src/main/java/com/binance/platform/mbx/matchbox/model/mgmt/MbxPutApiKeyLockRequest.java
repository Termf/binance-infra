package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 *
 * @author Li Fenggang
 * Date: 2020/7/14 7:31 上午
 */
public class MbxPutApiKeyLockRequest extends MbxBaseRequest {
    private long accountId;

    public MbxPutApiKeyLockRequest(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String getUri() {
        return "/v1/apiKey/lock";
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
