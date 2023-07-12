package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeleteUserDataStreamRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/userDataStream";
    }

    @NotEmpty
    private String listenKey;

    private long accountId;

    public MbxDeleteUserDataStreamRequest(@NotEmpty String listenKey, long accountId) {
        this.listenKey = listenKey;
        this.accountId = accountId;
    }

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
