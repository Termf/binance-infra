package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutSymbolRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;
    @NotEmpty
    private String symbolStatus;

    public PutSymbolRequest(@NotEmpty String symbol, @NotEmpty String symbolStatus) {
        this.symbol = symbol;
        this.symbolStatus = symbolStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolStatus() {
        return symbolStatus;
    }

    public void setSymbolStatus(String symbolStatus) {
        this.symbolStatus = symbolStatus;
    }
}