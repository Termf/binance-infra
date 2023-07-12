package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * symbol OR accountId must be sent, and both can be sent.
 */
public class GetMsgAuthOrdersRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private Long accountId;

    private String symbol;

    private long startTime;

    private Integer limit;

    @NotEmpty
    private String msgAuth;

    /**
     * msgAuth Supported Values: LIQUIDATION and MANUAL. Any other value will display an error. <br/>
     * symbol OR accountId must be sent, and both can be sent
     *
     * @param startTime
     * @param msgAuth
     */
    public GetMsgAuthOrdersRequest(long startTime, @NotEmpty String msgAuth) {
        this.startTime = startTime;
        this.msgAuth = msgAuth;
    }

    public Long getAccountId() {
        return accountId;
    }

    /** symbol OR accountId must be sent, and both can be sent. */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    /** symbol OR accountId must be sent, and both can be sent. */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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