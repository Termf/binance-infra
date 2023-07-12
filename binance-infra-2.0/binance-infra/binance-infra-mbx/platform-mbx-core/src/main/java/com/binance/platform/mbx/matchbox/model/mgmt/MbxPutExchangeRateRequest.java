package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutExchangeRateRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/exchangeRate";
    }

    @NotEmpty
    private String baseAsset;
    @NotEmpty
    private String price;
    @NotEmpty
    private String quoteAsset;
    private String invert;

    public MbxPutExchangeRateRequest(String baseAsset, String price, String quoteAsset) {
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
