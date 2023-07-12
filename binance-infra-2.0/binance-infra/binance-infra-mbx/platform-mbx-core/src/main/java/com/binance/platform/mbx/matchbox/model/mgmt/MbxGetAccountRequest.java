package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 2:01 下午
 */
public class MbxGetAccountRequest extends MbxBaseRequest {
    private long accountId;

    public MbxGetAccountRequest(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String getUri() {
        return "/v1/account";
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
