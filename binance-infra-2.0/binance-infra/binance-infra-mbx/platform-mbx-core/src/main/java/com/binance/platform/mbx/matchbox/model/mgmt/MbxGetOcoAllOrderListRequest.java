package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * Retrieving all OCOs from an account
 *
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetOcoAllOrderListRequest extends MbxBaseRequest {

    @Override
    public String getUri() {
        return "/v1/allOrderList";
    }

    private long accountId;
    private Long fromId;
    private Long startTime;
    private Long endTime;
    private Integer limit;

    public MbxGetOcoAllOrderListRequest(long accountId) {
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
