package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 11:20 下午
 */
public class RestGetAggTradesRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return null;
    }

    private String symbol;
    private Long startTime;
    private Long endTime;
    private Long limit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
