package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetEstimateSymbolRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/estimateSymbol";
    }

    @NotEmpty
    private String baseDecimalPlaces;
    @NotEmpty
    private String quoteDecimalPlaces;
    private String mathSystemType;
    private String maxPrice;
    private String maxQty;

    public MbxGetEstimateSymbolRequest(@NotEmpty String baseDecimalPlaces, @NotEmpty String quoteDecimalPlaces) {
        this.baseDecimalPlaces = baseDecimalPlaces;
        this.quoteDecimalPlaces = quoteDecimalPlaces;
    }

    public String getBaseDecimalPlaces() {
        return baseDecimalPlaces;
    }

    public void setBaseDecimalPlaces(String baseDecimalPlaces) {
        this.baseDecimalPlaces = baseDecimalPlaces;
    }

    public String getQuoteDecimalPlaces() {
        return quoteDecimalPlaces;
    }

    public void setQuoteDecimalPlaces(String quoteDecimalPlaces) {
        this.quoteDecimalPlaces = quoteDecimalPlaces;
    }

    public String getMathSystemType() {
        return mathSystemType;
    }

    public void setMathSystemType(String mathSystemType) {
        this.mathSystemType = mathSystemType;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }
}
