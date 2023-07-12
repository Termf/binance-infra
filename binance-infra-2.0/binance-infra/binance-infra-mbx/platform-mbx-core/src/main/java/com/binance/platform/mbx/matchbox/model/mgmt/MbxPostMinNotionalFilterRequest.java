package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostMinNotionalFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/minNotionalFilter";
    }

    @NotEmpty
    private String enableMarket;
    @NotEmpty
    private String minNotional;
    @NotEmpty
    private String symbol;

    public MbxPostMinNotionalFilterRequest(@NotEmpty String enableMarket, @NotEmpty String minNotional, @NotEmpty String symbol) {
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
