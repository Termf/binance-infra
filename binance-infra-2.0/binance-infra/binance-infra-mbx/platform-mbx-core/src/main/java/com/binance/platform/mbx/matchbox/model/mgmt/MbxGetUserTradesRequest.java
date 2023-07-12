package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetUserTradesRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/userTrades";
    }

    private long accountId;
    private Long endTime;
    private Long fromId;
    private Long limit;
    private Long startTime;
    private String symbol;

    public MbxGetUserTradesRequest(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
