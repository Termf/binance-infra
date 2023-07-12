package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostMinNotionalFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String enableMarket;
    @NotEmpty
    private String minNotional;
    @NotEmpty
    private String symbol;

    public PostMinNotionalFilterRequest(@NotEmpty String enableMarket, @NotEmpty String minNotional,
                                        @NotEmpty String symbol) {
        this.enableMarket = enableMarket;
        this.minNotional = minNotional;
        this.symbol = symbol;
    }

    public String getEnableMarket() {
        return enableMarket;
    }

    public void setEnableMarket(String enableMarket) {
        this.enableMarket = enableMarket;
    }

    public String getMinNotional() {
        return minNotional;
    }

    public void setMinNotional(String minNotional) {
        this.minNotional = minNotional;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}