package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:23 上午
 */
public class GetAssetBalanceResponse extends ToString {

    /**
     * asset : BTC
     * free : 49475243196.92864301
     * locked : 368.15378885
     * extLocked : 0.00000000
     */

    private String asset;
    private BigDecimal free;
    private BigDecimal locked;
    private BigDecimal extLocked;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public BigDecimal getLocked() {
        return locked;
    }

    public void setLocked(BigDecimal locked) {
        this.locked = locked;
    }

    public BigDecimal getExtLocked() {
        return extLocked;
    }

    public void setExtLocked(BigDecimal extLocked) {
        this.extLocked = extLocked;
    }
}
