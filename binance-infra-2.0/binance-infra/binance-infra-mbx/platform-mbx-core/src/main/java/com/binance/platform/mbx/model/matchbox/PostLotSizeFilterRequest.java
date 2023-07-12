package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostLotSizeFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String maxQty;
    @NotEmpty
    private String minQty;
    @NotEmpty
    private String stepSize;
    @NotEmpty
    private String symbol;

    public PostLotSizeFilterRequest(@NotEmpty String maxQty, @NotEmpty String minQty, @NotEmpty String stepSize,
                                    @NotEmpty String symbol) {
        this.maxQty = maxQty;
        this.minQty = minQty;
        this.stepSize = stepSize;
        this.symbol = symbol;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getStepSize() {
        return stepSize;
    }

    public void setStepSize(String stepSize) {
        this.stepSize = stepSize;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}