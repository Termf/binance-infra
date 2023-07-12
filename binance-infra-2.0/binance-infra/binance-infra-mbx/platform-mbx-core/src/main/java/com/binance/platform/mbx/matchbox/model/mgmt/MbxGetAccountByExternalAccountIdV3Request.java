package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetAccountByExternalAccountIdV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/accountByExternalAccountId";
    }

    private long externalAccountId;

    public MbxGetAccountByExternalAccountIdV3Request(long externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public long getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(long externalAccountId) {
        this.externalAccountId = externalAccountId;
    }
}
