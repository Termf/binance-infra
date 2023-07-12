package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:06 上午
 */
public class MbxGetAssetsResponse extends MbxBaseModel {
    /**
     * asset : BNB
     * decimalPlaces : 8
     */

    private String asset;
    private int decimalPlaces;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

}
