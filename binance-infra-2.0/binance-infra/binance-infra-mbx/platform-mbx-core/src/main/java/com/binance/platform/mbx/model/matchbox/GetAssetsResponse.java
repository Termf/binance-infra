package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:04 上午
 */
public class GetAssetsResponse extends ToString {
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
