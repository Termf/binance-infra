package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 9:49 下午
 */
public class MbxGetBalanceLedgerRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/balanceLedger";
    }

    private long accountId;
    @NotNull
    private String asset;
    private Long startTime;
    private Long endTime;
    private Long fromExternalUpdateId;
    private Long toExternalUpdateId;
    private Long externalUpdateId;

    public MbxGetBalanceLedgerRequest(long accountId, @NotNull String asset) {
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public Long getExternalUpdateId() {
        return externalUpdateId;
    }

    public void setExternalUpdateId(Long externalUpdateId) {
        this.externalUpdateId = externalUpdateId;
    }
}
