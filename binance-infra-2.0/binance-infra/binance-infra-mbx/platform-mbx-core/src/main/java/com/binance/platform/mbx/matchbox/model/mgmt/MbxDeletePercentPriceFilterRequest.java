package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeletePercentPriceFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/percentPriceFilter";
    }

    @NotEmpty
    private String symbol;

    public MbxDeletePercentPriceFilterRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}