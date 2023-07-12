package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostNumOrdersFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/numOrdersFilter";
    }

    @NotEmpty
    private String numOrders;
    @NotEmpty
    private String symbol;
    private String exemptAcct;

    public MbxPostNumOrdersFilterRequest(@NotEmpty String numOrders, @NotEmpty String symbol) {
        this.numOrders = numOrders;
        this.symbol = symbol;
    }

    public String getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(String numOrders) {
        this.numOrders = numOrders;
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
