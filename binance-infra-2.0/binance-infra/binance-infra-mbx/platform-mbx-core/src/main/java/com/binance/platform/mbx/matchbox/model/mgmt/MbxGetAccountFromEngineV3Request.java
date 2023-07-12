package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 2:01 下午
 */
public class MbxGetAccountFromEngineV3Request extends MbxBaseRequest {
    private long accountId;

    public MbxGetAccountFromEngineV3Request(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String getUri() {
        return "/v3/accountFromEngine";
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
