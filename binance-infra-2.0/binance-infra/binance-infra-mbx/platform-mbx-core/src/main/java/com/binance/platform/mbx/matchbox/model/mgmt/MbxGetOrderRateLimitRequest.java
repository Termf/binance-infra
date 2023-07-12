package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetOrderRateLimitRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/orderRateLimit";
    }

    @NotEmpty
    private String rateInterval;
    private String intervalNum;

    public MbxGetOrderRateLimitRequest(@NotEmpty String rateInterval) {
        this.rateInterval = rateInterval;
    }

    public String getRateInterval() {
        return rateInterval;
    }

    public void setRateInterval(String rateInterval) {
        this.rateInterval = rateInterval;
    }

    public String getIntervalNum() {
        return intervalNum;
    }

    public void setIntervalNum(String intervalNum) {
        this.intervalNum = intervalNum;
    }
}
