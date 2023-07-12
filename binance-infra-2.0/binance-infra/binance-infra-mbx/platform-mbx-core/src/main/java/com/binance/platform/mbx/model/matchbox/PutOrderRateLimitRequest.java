package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutOrderRateLimitRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long limit;
	@NotEmpty
	private String rateInterval;

	public PutOrderRateLimitRequest(long limit, @NotEmpty String rateInterval) {
		this.limit = limit;
		this.rateInterval = rateInterval;
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