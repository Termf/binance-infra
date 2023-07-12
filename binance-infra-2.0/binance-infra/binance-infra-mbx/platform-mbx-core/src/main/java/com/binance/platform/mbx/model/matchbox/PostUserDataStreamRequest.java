package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class PostUserDataStreamRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;

	public PostUserDataStreamRequest(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
}