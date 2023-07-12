package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostBalanceRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	@NotEmpty
	private String asset;
	@NotEmpty
	private String balanceDelta;
	@NotEmpty
	private String updateId;

	public PostBalanceRequest(long accountId, String asset, String balanceDelta, String updateId) {
		this.accountId = accountId;
		this.asset = asset;
		this.balanceDelta = balanceDelta;
		this.updateId = updateId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getBalanceDelta() {
		return balanceDelta;
	}

	public void setBalanceDelta(String balanceDelta) {
		this.balanceDelta = balanceDelta;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
}