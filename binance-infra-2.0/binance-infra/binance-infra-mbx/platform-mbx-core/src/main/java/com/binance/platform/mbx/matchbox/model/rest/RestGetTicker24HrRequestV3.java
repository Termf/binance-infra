package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * RestGetTicker24HrRequestV3
 *
 * @author Li Fenggang
 * Date: 2020/7/17 4:54 下午
 */
public class RestGetTicker24HrRequestV3 extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/ticker/24hr";
    }

    @NotEmpty
    private String symbol;

    public RestGetTicker24HrRequestV3(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
