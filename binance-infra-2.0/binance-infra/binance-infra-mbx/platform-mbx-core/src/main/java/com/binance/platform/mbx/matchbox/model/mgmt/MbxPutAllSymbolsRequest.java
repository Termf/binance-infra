package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutAllSymbolsRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/allSymbols";
    }

    @NotEmpty
    private String symbolStatus;

    public MbxPutAllSymbolsRequest(@NotEmpty String symbolStatus) {
        this.symbolStatus = symbolStatus;
    }

    public String getSymbolStatus() {
        return symbolStatus;
    }

    public void setSymbolStatus(String symbolStatus) {
        this.symbolStatus = symbolStatus;
    }
}
