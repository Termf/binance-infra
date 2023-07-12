package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutExchangeGasRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String asset;
	@NotEmpty
	private String bips;

	public PutExchangeGasRequest(@NotEmpty String asset, @NotEmpty String bips) {
		this.asset = asset;
		this.bips = bips;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getBips() {
		return bips;
	}

	public void setBips(String bips) {
		this.bips = bips;
	}
}