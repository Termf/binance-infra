package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostLotSizeFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/lotSizeFilter";
    }

    @NotEmpty
    private String maxQty;
    @NotEmpty
    private String minQty;
    @NotEmpty
    private String stepSize;
    @NotEmpty
    private String symbol;

    public MbxPostLotSizeFilterRequest(@NotEmpty String maxQty, @NotEmpty String minQty, @NotEmpty String stepSize,
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
