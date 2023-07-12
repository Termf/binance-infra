package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class ActivateAccountRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;

    public ActivateAccountRequest(long userId, boolean canTrade, boolean canWithdraw, boolean canDeposit) {
        this.userId = userId;
        this.canTrade = canTrade;
        this.canWithdraw = canWithdraw;
        this.canDeposit = canDeposit;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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