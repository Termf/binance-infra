package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutExchangeRateRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String baseAsset;
    @NotEmpty
    private String price;
    @NotEmpty
    private String quoteAsset;
    private String invert;

    public PutExchangeRateRequest(String baseAsset, String price, String quoteAsset) {
        this.baseAsset = baseAsset;
        this.price = price;
        this.quoteAsset = quoteAsset;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getInvert() {
        return invert;
    }

    public void setInvert(String invert) {
        this.invert = invert;
    }
}