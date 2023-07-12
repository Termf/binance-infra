package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostIcebergPartsFilterRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long limit;
	@NotEmpty
	private String symbol;

	public PostIcebergPartsFilterRequest(long limit, @NotEmpty String symbol) {
		this.limit = limit;
		this.symbol = symbol;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}