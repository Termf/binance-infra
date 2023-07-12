package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostPercentPriceFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String multiplierDown;
    @NotEmpty
    private String multiplierUp;
    @NotEmpty
    private String symbol;

    public PostPercentPriceFilterRequest(@NotEmpty String multiplierDown, @NotEmpty String multiplierUp,
                                         @NotEmpty String symbol) {
        this.multiplierDown = multiplierDown;
        this.multiplierUp = multiplierUp;
        this.symbol = symbol;
    }

    public String getMultiplierDown() {
        return multiplierDown;
    }

    public void setMultiplierDown(String multiplierDown) {
        this.multiplierDown = multiplierDown;
    }

    public String getMultiplierUp() {
        return multiplierUp;
    }

    public void setMultiplierUp(String multiplierUp) {
        this.multiplierUp = multiplierUp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}