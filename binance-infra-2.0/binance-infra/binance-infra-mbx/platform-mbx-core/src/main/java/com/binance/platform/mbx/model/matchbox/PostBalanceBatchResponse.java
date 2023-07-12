package com.binance.platform.mbx.model.matchbox;

import java.math.BigDecimal;

public class PostBalanceBatchResponse {
    /**
     * success : true
     * accountId : 2172
     * externalAccountId : 350790515
     * time : 1596963446658
     * mbxUpdateId : 35428
     * externalUpdateId : 1
     * asset : BTC
     * adjustmentAmount : 1.00000000
     * free : 103.99956500
     * locked : -5.00000000
     * extLocked : 0.00000000
     */

    private boolean success;
    private Long accountId;
    private Long externalAccountId;
    private Long time;
    private Long mbxUpdateId;
    private Long externalUpdateId;
    private String asset;
    private BigDecimal adjustmentAmount;
    private BigDecimal free;
    private BigDecimal locked;
    private BigDecimal extLocked;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(Long externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getMbxUpdateId() {
        return mbxUpdateId;
    }

    public void setMbxUpdateId(Long mbxUpdateId) {
        this.mbxUpdateId = mbxUpdateId;
    }

    public Long getExternalUpdateId() {
        return externalUpdateId;
    }

    public void setExternalUpdateId(Long externalUpdateId) {
        this.externalUpdateId = externalUpdateId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
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