package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetSymbolHistoryRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long startTime;
    @NotEmpty
    private String symbol;

    public GetSymbolHistoryRequest(long startTime, String symbol) {
        this.startTime = startTime;
        this.symbol = symbol;
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