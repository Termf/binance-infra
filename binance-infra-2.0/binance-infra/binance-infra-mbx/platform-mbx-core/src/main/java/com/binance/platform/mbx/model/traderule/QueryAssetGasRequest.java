package com.binance.platform.mbx.model.traderule;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * set asset gas: input url and type Request
 */
public class QueryAssetGasRequest extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = -4683534496637603921L;

    @NotNull
    private String asset;
    @NotNull
    private BigDecimal rate;

    public QueryAssetGasRequest(@NotNull String asset, @NotNull BigDecimal rate) {
        this.asset = asset;
        this.rate = rate;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
