package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeleteTPlusSellFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/tPlusSellFilter";
    }

    @NotEmpty
    private String symbol;
    private String force;

    public MbxDeleteTPlusSellFilterRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }
}
