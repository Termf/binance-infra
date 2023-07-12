package com.binance.platform.mbx.matchbox.model.mgmt;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 10:09 下午
 */
public class MbxGetBalanceLedgerResponse {
    /**
     * accountId : 0
     * asset : ABC
     * balanceDelta : 10000.000000
     * time : 1566194639896
     * updateId : 4
     * externalUpdateId : 4
     * mbxUpdateId : 5
     */
    private long accountId;
    private String asset;
    private BigDecimal balanceDelta;
    private Long time;
    private Long updateId;
    private Long externalUpdateId;
    private Long mbxUpdateId;

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
