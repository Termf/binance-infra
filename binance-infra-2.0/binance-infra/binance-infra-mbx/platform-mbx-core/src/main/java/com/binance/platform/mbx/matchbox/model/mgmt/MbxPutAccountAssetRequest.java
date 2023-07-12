package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/8 2:46 下午
 */
public class MbxPutAccountAssetRequest extends MbxBaseRequest {
    private long accountId;
    @NotEmpty
    private String asset;
    @NotEmpty
    private String symbolType;
    @NotNull
    private BigDecimal locked;
    @NotNull
    private BigDecimal available;

    public MbxPutAccountAssetRequest(long accountId, @NotEmpty String asset, @NotEmpty String symbolType,
                                     @NotNull BigDecimal locked, @NotNull BigDecimal available) {
        this.accountId = accountId;
        this.asset = asset;
        this.symbolType = symbolType;
        this.locked = locked;
        this.available = available;
    }

    @Override
    public String getUri() {
        return "/v1/account/asset";
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

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public BigDecimal getLocked() {
        return locked;
    }

    public void setLocked(BigDecimal locked) {
        this.locked = locked;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

}
