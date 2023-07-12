package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostPriceFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String maxPrice;
    @NotEmpty
    private String minPrice;
    @NotEmpty
    private String symbol;
    @NotEmpty
    private String tickSize;

    public PostPriceFilterRequest(String maxPrice, String minPrice, String symbol, String tickSize) {
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