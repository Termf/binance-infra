package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutExchangeGasRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/exchangeGas";
    }

    @NotEmpty
    private String asset;
    @NotEmpty
    private String bips;

    public MbxPutExchangeGasRequest(@NotEmpty String asset, @NotEmpty String bips) {
        this.asset = asset;
        this.bips = bips;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getBips() {
        return bips;
    }

    public void setBips(String bips) {
        this.bips = bips;
    }
}
