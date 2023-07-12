package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PutAccountAssetRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    @NotEmpty
    private String asset;
    @NotNull
    private BigDecimal available;
    @NotNull
    private BigDecimal locked;
    @NotEmpty
    private String symbolType;

    public PutAccountAssetRequest(long accountId, String asset, BigDecimal available, BigDecimal locked, String symbolType) {
        this.accountId = accountId;
        this.asset = asset;
        this.available = available;
        this.locked = locked;
        this.symbolType = symbolType;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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

    public BigDecimal getLocked() {
        return locked;
    }

    public void setLocked(BigDecimal locked) {
        this.locked = locked;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }
}