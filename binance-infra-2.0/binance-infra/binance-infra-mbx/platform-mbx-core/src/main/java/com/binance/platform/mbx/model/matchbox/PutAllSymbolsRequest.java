package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutAllSymbolsRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String symbolStatus;

	public PutAllSymbolsRequest(@NotEmpty String symbolStatus) {
		this.symbolStatus = symbolStatus;
	}

	public String getSymbolStatus() {
		return symbolStatus;
	}

	public void setSymbolStatus(String symbolStatus) {
		this.symbolStatus = symbolStatus;
	}
}