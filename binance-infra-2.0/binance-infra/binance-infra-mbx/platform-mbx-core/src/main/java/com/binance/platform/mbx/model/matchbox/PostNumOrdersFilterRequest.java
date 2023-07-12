package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostNumOrdersFilterRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String numOrders;
	@NotEmpty
	private String symbol;
	private String exemptAcct;

	public PostNumOrdersFilterRequest(@NotEmpty String numOrders, @NotEmpty String symbol) {
		this.numOrders = numOrders;
		this.symbol = symbol;
	}

	public String getNumOrders() {
		return numOrders;
	}

	public void setNumOrders(String numOrders) {
		this.numOrders = numOrders;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExemptAcct() {
		return exemptAcct;
	}

	public void setExemptAcct(String exemptAcct) {
		this.exemptAcct = exemptAcct;
	}
}