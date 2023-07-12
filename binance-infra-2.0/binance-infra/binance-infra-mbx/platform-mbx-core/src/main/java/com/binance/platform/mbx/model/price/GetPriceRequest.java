package com.binance.platform.mbx.model.price;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetPriceRequest extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = -4108532536126753286L;

    @NotEmpty
    private String symbol;

    public GetPriceRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
