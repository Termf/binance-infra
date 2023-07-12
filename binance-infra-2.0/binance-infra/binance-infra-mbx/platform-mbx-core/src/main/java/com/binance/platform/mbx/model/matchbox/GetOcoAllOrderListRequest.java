package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetOcoAllOrderListRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private Long fromId;
    private Long startTime;
    private Long endTime;
    private Integer limit;

    public GetOcoAllOrderListRequest(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}