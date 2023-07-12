package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 4:54 下午
 */
public class RestGetTicker24HrRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/ticker/24hr";
    }

    @NotEmpty
    private String symbol;

    public RestGetTicker24HrRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
