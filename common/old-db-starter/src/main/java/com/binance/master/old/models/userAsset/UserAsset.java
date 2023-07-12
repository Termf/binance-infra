package com.binance.master.old.models.userAsset;

import java.math.BigDecimal;
import java.util.Date;

public class UserAsset extends UserAssetKey {
    private BigDecimal free;

    private BigDecimal locked;

    private BigDecimal freeze;

    private BigDecimal withdrawing;

    private Date modifiedAt;

    private Date createdAt;

    private BigDecimal ipoing;

    private BigDecimal ipoable;

    private BigDecimal storage;

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

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public BigDecimal getWithdrawing() {
        return withdrawing;
    }

    public void setWithdrawing(BigDecimal withdrawing) {
        this.withdrawing = withdrawing;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getIpoing() {
        return ipoing;
    }

    public void setIpoing(BigDecimal ipoing) {
        this.ipoing = ipoing;
    }

    public BigDecimal getIpoable() {
        return ipoable;
    }

    public void setIpoable(BigDecimal ipoable) {
        this.ipoable = ipoable;
    }

    public BigDecimal getStorage() {
        return storage;
    }

    public void setStorage(BigDecimal storage) {
        this.storage = storage;
    }
}
