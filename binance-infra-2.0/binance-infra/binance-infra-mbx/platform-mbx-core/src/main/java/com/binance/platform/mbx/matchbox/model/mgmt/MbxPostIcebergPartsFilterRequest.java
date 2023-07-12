package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostIcebergPartsFilterRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/icebergPartsFilter";
    }

    private long limit;
    @NotEmpty
    private String symbol;

    public MbxPostIcebergPartsFilterRequest(long limit, @NotEmpty String symbol) {
        this.limit = limit;
        this.symbol = symbol;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
