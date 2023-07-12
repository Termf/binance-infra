package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class PutAccountRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private boolean canDeposit;
    private boolean canTrade;
    private boolean canWithdraw;

    private String accountType;

    public PutAccountRequest(long accountId, boolean canDeposit, boolean canTrade, boolean canWithdraw) {
        this.accountId = accountId;
        this.canDeposit = canDeposit;
        this.canTrade = canTrade;
        this.canWithdraw = canWithdraw;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public boolean isCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(boolean canDeposit) {
        this.canDeposit = canDeposit;
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

    public String getAccountType() {
        return accountType;
    }

    /**
     * values: SPOT and MARGIN.
     * accountType defaults to SPOT.
     * accountType cannot be changed once set on an account.
     * Accounts with the MARGIN accountType can only trade on symbols that allow margin trading.
     * <p>
     * accountType is included on all OutboundAccountInfo events from the private streamer.
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}