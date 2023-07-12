package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/15 5:54 下午
 */
public class MbxPostBalanceRequest extends MbxBaseRequest {

    private long accountId;
    @NotEmpty
    private String asset;
    @NotEmpty
    private String balanceDelta;
    @NotEmpty
    private String updateId;

    public MbxPostBalanceRequest(long accountId, String asset, String balanceDelta, String updateId) {
        this.accountId = accountId;
        this.asset = asset;
        this.balanceDelta = balanceDelta;
        this.updateId = updateId;
    }

    @Override
    public String getUri() {
        return "/v1/balance";
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getBalanceDelta() {
        return balanceDelta;
    }

    public void setBalanceDelta(String balanceDelta) {
        this.balanceDelta = balanceDelta;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
}
