package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/16 4:34 下午
 */
public class RestGetAvgPriceV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/avgPrice";
    }

    @NotEmpty
    private String symbol;

    public RestGetAvgPriceV3Request(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
