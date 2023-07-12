package com.binance.platform.mbx.model.trade;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetAggTradesRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = 5948724134750989527L;
    /**
     * 交易对
     */
    @NotEmpty
    private String symbol;
    /**
     * 开始时间戳
     */
    private Long startTime;
    /**
     * 截至时间戳
     */
    private Long endTime;
    private Long limit;

    public GetAggTradesRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

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
