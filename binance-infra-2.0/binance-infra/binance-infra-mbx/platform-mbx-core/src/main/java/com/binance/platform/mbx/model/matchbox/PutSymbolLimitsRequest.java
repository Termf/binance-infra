package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PutSymbolLimitsRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;

    @NotNull
    private BigDecimal minQty;

    @NotNull
    private BigDecimal maxPrice;

    @NotNull
    private BigDecimal maxQty;

    public PutSymbolLimitsRequest(String symbol, BigDecimal minQty, BigDecimal maxPrice, BigDecimal maxQty) {
        this.symbol = symbol;
        this.minQty = minQty;
        this.maxPrice = maxPrice;
        this.maxQty = maxQty;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getMinQty() {
        return minQty;
    }

    public void setMinQty(BigDecimal minQty) {
        this.minQty = minQty;
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