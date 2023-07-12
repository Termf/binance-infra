package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 11:36 下午
 */
public class MbxPostBalanceResponse extends MbxBaseModel {

    /**
     * accountId : 12530
     * externalAccountId : 350801020
     * time : 1596525393339
     * mbxUpdateId : 30739
     * externalUpdateId : 15
     * asset : BNB
     * adjustmentAmount : 1.00000000
     * free : 18.00000000
     * locked : 0.00000000
     * extLocked : 0.00000000
     */

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
