package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetSymbolCommissionHistoryRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long endTime;
    private long startTime;
    @NotEmpty
    private String symbol;

    public GetSymbolCommissionHistoryRequest(long endTime, long startTime, @NotEmpty String symbol) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.symbol = symbol;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}