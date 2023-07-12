package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:46 上午
 */
public class MbxGetCommissionResponse extends MbxBaseModel {

    /**
     * asset : BTC
     * commission : 0.62308271
     */

    private String asset;
    private BigDecimal commission;

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }
}
