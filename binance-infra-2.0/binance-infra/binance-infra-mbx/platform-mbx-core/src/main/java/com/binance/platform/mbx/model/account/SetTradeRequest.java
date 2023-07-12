package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class SetTradeRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;
    private boolean canTrade;

    public SetTradeRequest(long userId, boolean canTrade) {
        this.userId = userId;
        this.canTrade = canTrade;
    }

    /**
     * values: SPOT and MARGIN.
     * accountType defaults to SPOT.
     * accountType cannot be changed once set on an account.
     * Accounts with the MARGIN accountType can only trade on symbols that allow margin trading.
     *
     * accountType is included on all OutboundAccountInfo events from the private streamer.
     */
    private String accountType;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean getCanTrade() {
        return canTrade;
    }

    public void setCanTrade(boolean canTrade) {
        this.canTrade = canTrade;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}