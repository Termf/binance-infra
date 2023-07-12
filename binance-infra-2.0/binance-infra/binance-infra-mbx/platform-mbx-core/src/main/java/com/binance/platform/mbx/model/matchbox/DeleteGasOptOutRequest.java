package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class DeleteGasOptOutRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;

	public DeleteGasOptOutRequest(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
}