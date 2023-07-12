package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetAssetBalanceRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String asset;

	public GetAssetBalanceRequest(@NotEmpty String asset) {
		this.asset = asset;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}
}