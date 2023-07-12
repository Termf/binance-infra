package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetOrderRateLimitRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String rateInterval;
	private String intervalNum;

	public GetOrderRateLimitRequest(@NotEmpty String rateInterval) {
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