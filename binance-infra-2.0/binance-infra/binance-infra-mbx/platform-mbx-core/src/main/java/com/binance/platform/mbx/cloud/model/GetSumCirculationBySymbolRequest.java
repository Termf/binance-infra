package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetSumCirculationBySymbolRequest extends ToString {
    private static final long serialVersionUID = -5268978363705093854L;
    @NotEmpty
    private String symbol;

    public GetSumCirculationBySymbolRequest() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }
}