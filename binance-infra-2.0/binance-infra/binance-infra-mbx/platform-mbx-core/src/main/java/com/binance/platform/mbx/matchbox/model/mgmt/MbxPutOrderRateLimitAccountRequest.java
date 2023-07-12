package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutOrderRateLimitAccountRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/orderRateLimit/account";
    }

    private long accountId;
    private long limit;
    @NotEmpty
    private String rateInterval;

    public MbxPutOrderRateLimitAccountRequest(long accountId, long limit, @NotEmpty String rateInterval) {
        this.accountId = accountId;
        this.limit = limit;
        this.rateInterval = rateInterval;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getRateInterval() {
        return rateInterval;
    }

    public void setRateInterval(String rateInterval) {
        this.rateInterval = rateInterval;
    }
}
