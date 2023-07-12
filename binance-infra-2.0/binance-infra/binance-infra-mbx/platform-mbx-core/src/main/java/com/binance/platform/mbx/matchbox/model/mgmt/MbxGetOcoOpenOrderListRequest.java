package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * Retrieving all Open OCOs
 *
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetOcoOpenOrderListRequest extends MbxBaseRequest {

    @Override
    public String getUri() {
        return "/v1/openOrderList";
    }

    private long accountId;

    public MbxGetOcoOpenOrderListRequest(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
