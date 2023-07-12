package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostPositionFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/positionFilter";
    }

    @NotEmpty
    private String maxQty;
    @NotEmpty
    private String symbol;
    private String exemptAcct;

    public MbxPostPositionFilterRequest(@NotEmpty String maxQty, @NotEmpty String symbol) {
        this.maxQty = maxQty;
        this.symbol = symbol;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExemptAcct() {
        return exemptAcct;
    }

    public void setExemptAcct(String exemptAcct) {
        this.exemptAcct = exemptAcct;
    }
}
