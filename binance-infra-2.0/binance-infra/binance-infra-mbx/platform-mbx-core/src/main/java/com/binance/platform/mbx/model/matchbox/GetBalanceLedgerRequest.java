package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetBalanceLedgerRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    @NotEmpty
    private String asset;
    private Long endTime;
    private Long startTime;
    private Long fromExternalUpdateId;
    private Long toExternalUpdateId;

    public GetBalanceLedgerRequest(long accountId, @NotEmpty String asset) {
        this.accountId = accountId;
        this.asset = asset;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getFromExternalUpdateId() {
        return fromExternalUpdateId;
    }

    public void setFromExternalUpdateId(Long fromExternalUpdateId) {
        this.fromExternalUpdateId = fromExternalUpdateId;
    }

    public Long getToExternalUpdateId() {
        return toExternalUpdateId;
    }

    public void setToExternalUpdateId(Long toExternalUpdateId) {
        this.toExternalUpdateId = toExternalUpdateId;
    }
}