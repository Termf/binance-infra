package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class DeleteNumIcebergOrdersRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String symbol;

	public DeleteNumIcebergOrdersRequest(@NotEmpty String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}