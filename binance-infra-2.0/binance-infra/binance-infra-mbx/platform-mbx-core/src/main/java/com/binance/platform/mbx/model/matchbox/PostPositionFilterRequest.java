package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostPositionFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String maxQty;
    @NotEmpty
    private String symbol;
    private String exemptAcct;

	public PostPositionFilterRequest(@NotEmpty String maxQty, @NotEmpty String symbol) {
		this.maxQty = maxQty;
		this.symbol = symbol;
	}

	public String getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
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