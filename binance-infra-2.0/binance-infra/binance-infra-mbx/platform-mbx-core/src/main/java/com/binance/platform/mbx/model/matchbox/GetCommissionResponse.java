package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:41 上午
 */
public class GetCommissionResponse extends ToString {

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
