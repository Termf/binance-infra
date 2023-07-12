package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetSymbolHistoryRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbolHistory";
    }

    private long startTime;
    @NotEmpty
    private String symbol;

    public MbxGetSymbolHistoryRequest(long startTime, String symbol) {
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
