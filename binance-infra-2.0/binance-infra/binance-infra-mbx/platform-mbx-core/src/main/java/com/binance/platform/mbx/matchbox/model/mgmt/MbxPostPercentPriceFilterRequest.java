package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostPercentPriceFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/percentPriceFilter";
    }

    @NotEmpty
    private String multiplierDown;
    @NotEmpty
    private String multiplierUp;
    @NotEmpty
    private String symbol;

    public MbxPostPercentPriceFilterRequest(@NotEmpty String multiplierDown, @NotEmpty String multiplierUp,
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
