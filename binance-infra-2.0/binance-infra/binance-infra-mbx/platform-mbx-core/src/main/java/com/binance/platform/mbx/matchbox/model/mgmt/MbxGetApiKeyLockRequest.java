package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 *
 * @author Li Fenggang
 * Date: 2020/7/14 7:31 上午
 */
public class MbxGetApiKeyLockRequest extends MbxBaseRequest {
    private long accountId;

    public MbxGetApiKeyLockRequest(long accountId) {
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
