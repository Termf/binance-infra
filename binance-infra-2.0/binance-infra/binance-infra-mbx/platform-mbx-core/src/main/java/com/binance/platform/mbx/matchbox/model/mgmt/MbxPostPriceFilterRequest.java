package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostPriceFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/priceFilter";
    }

    @NotEmpty
    private String maxPrice;
    @NotEmpty
    private String minPrice;
    @NotEmpty
    private String symbol;
    @NotEmpty
    private String tickSize;

    public MbxPostPriceFilterRequest(String maxPrice, String minPrice, String symbol, String tickSize) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.symbol = symbol;
        this.tickSize = tickSize;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTickSize() {
        return tickSize;
    }

    public void setTickSize(String tickSize) {
        this.tickSize = tickSize;
    }
}
