package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostAssetRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String asset;
    @NotEmpty
    private String decimalPlaces;

    public PostAssetRequest(@NotEmpty String asset, @NotEmpty String decimalPlaces) {
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