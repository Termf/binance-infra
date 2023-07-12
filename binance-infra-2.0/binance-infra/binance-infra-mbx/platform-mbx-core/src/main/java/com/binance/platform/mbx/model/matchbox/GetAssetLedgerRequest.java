package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetAssetLedgerRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String asset;
    private long startTime;
    private long endTime;

    public GetAssetLedgerRequest(@NotEmpty String asset, long startTime, long endTime) {
        this.asset = asset;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}