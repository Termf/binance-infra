package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:29 上午
 */
public class GetAssetLedgerResponse extends ToString {

    /**
     * accountId : 959
     * asset : BTC
     * balanceDelta : -0.00002600
     * time : 1596427526948
     * updateId : 3039530957
     * externalUpdateId : 3039530957
     * mbxUpdateId : 29650
     */

    private Long accountId;
    private String asset;
    private String balanceDelta;
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

    public String getBalanceDelta() {
        return balanceDelta;
    }

    public void setBalanceDelta(String balanceDelta) {
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
