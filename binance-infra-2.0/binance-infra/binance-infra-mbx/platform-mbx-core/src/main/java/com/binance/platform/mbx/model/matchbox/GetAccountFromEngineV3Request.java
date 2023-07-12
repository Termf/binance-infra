package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetAccountFromEngineV3Request extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;

	/**
	 *
	 * @param accountId accountId in matchbox
	 */
	public GetAccountFromEngineV3Request(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
}