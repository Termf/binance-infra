package com.binance.platform.mbx.model.product;


import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * 创建Asset的请求对象
 */
public class CreateAssetRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = -4261684340340465004L;
    /**
     * 用户资产
     */
    @NotEmpty
    private String asset;
    @NotEmpty
    private String decimalPlaces;

    public CreateAssetRequest(@NotEmpty String asset, @NotEmpty String decimalPlaces) {
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
