package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * 撮合出入金
 */
public class BalanceLedgerResponse extends ToString {

    private Long accountId;
    private String asset;
    private BigDecimal balanceDelta;
    private Long time;
    private Long updateId;
    private Long externalUpdateId;
    private Long mbxUpdateId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getBalanceDelta() {
        return balanceDelta;
    }

    public void setBalanceDelta(BigDecimal balanceDelta) {
        this.balanceDelta = balanceDelta;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Long getExternalUpdateId() {
        return externalUpdateId;
    }

    public void setExternalUpdateId(Long externalUpdateId) {
        this.externalUpdateId = externalUpdateId;
    }

    public Long getMbxUpdateId() {
        return mbxUpdateId;
    }

    public void setMbxUpdateId(Long mbxUpdateId) {
        this.mbxUpdateId = mbxUpdateId;
    }
}