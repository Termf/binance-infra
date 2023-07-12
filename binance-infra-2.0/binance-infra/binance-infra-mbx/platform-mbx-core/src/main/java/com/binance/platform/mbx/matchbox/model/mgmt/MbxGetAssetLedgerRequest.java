package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetAssetLedgerRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/assetLedger";
    }

    @NotEmpty
    private String asset;
    private long startTime;
    private long endTime;

    public MbxGetAssetLedgerRequest(@NotEmpty String asset, long startTime, long endTime) {
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
