package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutSymbolLimitsRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbolLimits";
    }

    @NotEmpty
    private String symbol;

    @NotNull
    private BigDecimal minQty;

    @NotNull
    private BigDecimal maxPrice;

    @NotNull
    private BigDecimal maxQty;

    public MbxPutSymbolLimitsRequest(String symbol, BigDecimal minQty, BigDecimal maxPrice, BigDecimal maxQty) {
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
