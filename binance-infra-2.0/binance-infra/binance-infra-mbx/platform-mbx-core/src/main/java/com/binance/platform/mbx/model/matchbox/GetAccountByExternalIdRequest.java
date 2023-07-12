package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetAccountByExternalIdRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long externalId;

	public GetAccountByExternalIdRequest(long externalId) {
		this.externalId = externalId;
	}

	public long getExternalId() {
		return externalId;
	}

	public void setExternalId(long externalId) {
		this.externalId = externalId;
	}
}