package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutOrderRateLimitAccountRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	private long limit;
	@NotEmpty
	private String rateInterval;

	public PutOrderRateLimitAccountRequest(long accountId, long limit, @NotEmpty String rateInterval) {
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