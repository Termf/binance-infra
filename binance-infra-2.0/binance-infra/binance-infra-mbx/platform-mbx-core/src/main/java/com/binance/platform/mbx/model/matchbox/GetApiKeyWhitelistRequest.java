package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetApiKeyWhitelistRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long keyId;
	private long accountId;

	public GetApiKeyWhitelistRequest(long keyId, long accountId) {
		this.keyId = keyId;
		this.accountId = accountId;
	}

	public long getKeyId() {
		return keyId;
	}

	public void setKeyId(long keyId) {
		this.keyId = keyId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
}