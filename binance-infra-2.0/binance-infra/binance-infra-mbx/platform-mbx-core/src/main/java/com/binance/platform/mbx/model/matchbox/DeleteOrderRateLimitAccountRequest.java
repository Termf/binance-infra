package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class DeleteOrderRateLimitAccountRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	@NotEmpty
	private String rateInterval;

	public DeleteOrderRateLimitAccountRequest(long accountId, @NotEmpty String rateInterval) {
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