package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutSymbolRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbol";
    }

    @NotEmpty
    private String symbol;
    @NotEmpty
    private String symbolStatus;

    public MbxPutSymbolRequest(@NotEmpty String symbol, @NotEmpty String symbolStatus) {
        this.symbol = symbol;
        this.symbolStatus = symbolStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolStatus() {
        return symbolStatus;
    }

    public void setSymbolStatus(String symbolStatus) {
        this.symbolStatus = symbolStatus;
    }
}
