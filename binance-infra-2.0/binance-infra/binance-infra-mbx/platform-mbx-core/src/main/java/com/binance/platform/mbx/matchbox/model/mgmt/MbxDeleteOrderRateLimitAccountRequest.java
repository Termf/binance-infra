package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxDeleteOrderRateLimitAccountRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/orderRateLimit/account";
    }

    private long accountId;
    @NotEmpty
    private String rateInterval;

    public MbxDeleteOrderRateLimitAccountRequest(long accountId, @NotEmpty String rateInterval) {
        this.accountId = accountId;
        this.rateInterval = rateInterval;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getRateInterval() {
        return rateInterval;
    }

    public void setRateInterval(String rateInterval) {
        this.rateInterval = rateInterval;
    }
}
