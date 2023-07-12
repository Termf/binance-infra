package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 11:42 下午
 */
public class GetBalanceLedgerResponse extends ToString {

    /**
     * accountId : 2172
     * asset : BTC
     * balanceDelta : 1.00000000
     * time : 1596510632841
     * updateId : 13
     * externalUpdateId : 13
     * mbxUpdateId : 30658
     */

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
