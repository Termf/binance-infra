package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 1:32 下午
 */
public class MbxPutAccountRequest extends MbxBaseRequest {
    private long accountId;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;

    public MbxPutAccountRequest(long accountId, boolean canTrade, boolean canWithdraw, boolean canDeposit) {
        this.accountId = accountId;
        this.canTrade = canTrade;
        this.canWithdraw = canWithdraw;
        this.canDeposit = canDeposit;
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

    public boolean isCanTrade() {
        return canTrade;
    }

    public void setCanTrade(boolean canTrade) {
        this.canTrade = canTrade;
    }

    public boolean isCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public boolean isCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(boolean canDeposit) {
        this.canDeposit = canDeposit;
    }
}
