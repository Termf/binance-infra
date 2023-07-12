package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class GetBlanceLedgerRequest extends ToString {

    private static final long serialVersionUID = 7139552616128812527L;

    private long userId;
    @NotEmpty
    private String asset;
    private Date startTime;
    private Date endTime;
    private Long fromExternalUpdateId;
    private Long toExternalUpdateId;

    public GetBlanceLedgerRequest(long userId, @NotEmpty String asset) {
        this.userId = userId;
        this.asset = asset;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
}