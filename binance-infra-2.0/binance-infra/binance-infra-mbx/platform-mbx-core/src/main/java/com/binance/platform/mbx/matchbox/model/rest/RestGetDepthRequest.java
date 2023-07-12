package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 7:32 下午
 */
public class RestGetDepthRequest extends MbxBaseRequest {
    private String symbol;
    private Integer limit;

    @Override
    public String getUri() {
        return "/v1/depth";
    }

    public RestGetDepthRequest(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
