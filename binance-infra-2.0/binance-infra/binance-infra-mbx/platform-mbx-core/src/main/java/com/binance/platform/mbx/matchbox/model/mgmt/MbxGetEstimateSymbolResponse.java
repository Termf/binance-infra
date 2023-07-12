package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:13 上午
 */
public class MbxGetEstimateSymbolResponse extends MbxBaseModel {

    /**
     * baseDecimalPlaces : 8
     * quoteDecimalPlaces : 8
     * maxPrice : 92.23372036
     * maxQty : 10
     */

    private Long baseDecimalPlaces;
    private Long quoteDecimalPlaces;
    private BigDecimal maxPrice;
    private BigDecimal maxQty;

    public Long getBaseDecimalPlaces() {
        return baseDecimalPlaces;
    }

    public void setBaseDecimalPlaces(Long baseDecimalPlaces) {
        this.baseDecimalPlaces = baseDecimalPlaces;
    }

    public Long getQuoteDecimalPlaces() {
        return quoteDecimalPlaces;
    }

    public void setQuoteDecimalPlaces(Long quoteDecimalPlaces) {
        this.quoteDecimalPlaces = quoteDecimalPlaces;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(BigDecimal maxQty) {
        this.maxQty = maxQty;
    }
}
