package com.binance.platform.mbx.model.product;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class CreateAssetResponse extends MbxBaseModel {
    /**
     * asset : 1528700331071
     * free : 0.000000
     * locked : 10.000000
     */

    private String asset;
    private String free;
    private String locked;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
}
