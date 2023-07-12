package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostAssetRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/asset";
    }

    @NotEmpty
    private String asset;
    @NotEmpty
    private String decimalPlaces;

    public MbxPostAssetRequest(@NotEmpty String asset, @NotEmpty String decimalPlaces) {
        this.asset = asset;
        this.decimalPlaces = decimalPlaces;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
