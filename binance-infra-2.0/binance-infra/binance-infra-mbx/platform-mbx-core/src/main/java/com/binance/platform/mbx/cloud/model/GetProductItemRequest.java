package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

public class GetProductItemRequest extends ToString {
    private static final long serialVersionUID = -5960034782956682008L;
    private String symbol;

    public GetProductItemRequest() {
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }
}