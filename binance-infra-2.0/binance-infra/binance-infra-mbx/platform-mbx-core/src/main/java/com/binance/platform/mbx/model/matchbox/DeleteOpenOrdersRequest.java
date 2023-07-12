package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class DeleteOpenOrdersRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    @NotEmpty
    private String symbol;
    private String force;
    private String msgAuth;

    public DeleteOpenOrdersRequest(long accountId, @NotEmpty String symbol) {
        this.accountId = accountId;
        this.symbol = symbol;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getMsgAuth() {
        return msgAuth;
    }

    /**
     * values:
     * NORMAL
     * LEGACY_FORCE
     * LIQUIDATION
     * MANUAL
     * When msgAuth is not sent, it defaults to NORMAL.
     * When the msgAuth is set to anything other than NORMAL, the order or cancel will break tradingPhase, accountType, and symbol permission rules.
     * When the msgAuth is set to LIQUIDATION or MANUAL, those orders can be queried using the msgAuthOrders endpoint.
     * NORMAL and LEGACY_FORCE orders are not specially tracked.
     * MANUAL is intended to be used by the Core Tech team if needed in emergency situations. Please do not use msgAuth=MANUAL.
     * msgAuth is also included now in all executionReports coming from the private streamer.
     * The msgAuth for an exeuctionReport is determined by the parent order, so if an order is placed with msgAuth=LIQUIDATION, all trade executionReports from that order will have msgAuth=LIQUIDATION.
     */
    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }
}