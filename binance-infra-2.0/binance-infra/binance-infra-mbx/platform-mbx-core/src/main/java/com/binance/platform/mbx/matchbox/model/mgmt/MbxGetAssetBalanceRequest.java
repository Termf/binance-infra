package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetAssetBalanceRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/assetBalance";
    }

    @NotEmpty
    private String asset;

    public MbxGetAssetBalanceRequest(@NotEmpty String asset) {
        this.asset = asset;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }
}
