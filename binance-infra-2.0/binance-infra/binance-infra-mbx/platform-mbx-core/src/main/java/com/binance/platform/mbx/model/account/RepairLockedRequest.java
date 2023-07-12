package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RepairLockedRequest extends ToString {

    private static final long serialVersionUID = 7139552616128812526L;

    private long userId;
    @NotEmpty
    private String asset;
    @NotNull
    private BigDecimal available;

    public RepairLockedRequest(long userId, @NotEmpty String asset, @NotNull BigDecimal available) {
        this.userId = userId;
        this.asset = asset;
        this.available = available;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }
}