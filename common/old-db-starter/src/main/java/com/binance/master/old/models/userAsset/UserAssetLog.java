package com.binance.master.old.models.userAsset;

import java.math.BigDecimal;

public class UserAssetLog extends UserAssetLogKey {
    private Long tranId;

    private String uid;

    private String asset;

    private Integer type;

    private BigDecimal delta;

    private BigDecimal free;

    private BigDecimal locked;

    private BigDecimal freeze;

    private BigDecimal withdrawing;

    private BigDecimal ipoing;

    private BigDecimal ipoable;

    private BigDecimal storage;

    private String info;

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getDelta() {
        return delta;
    }

    public void setDelta(BigDecimal delta) {
        this.delta = delta;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }
}
