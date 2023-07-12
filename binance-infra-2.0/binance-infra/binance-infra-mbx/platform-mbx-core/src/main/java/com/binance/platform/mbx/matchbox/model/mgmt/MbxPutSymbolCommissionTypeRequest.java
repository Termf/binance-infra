package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutSymbolCommissionTypeRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbolCommissionType";
    }

    @NotEmpty
    private String commissionType;
    @NotEmpty
    private String symbol;

    public MbxPutSymbolCommissionTypeRequest(@NotEmpty String commissionType, @NotEmpty String symbol) {
        this.commissionType = commissionType;
        this.symbol = symbol;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
