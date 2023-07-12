package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostTPlusSellFilterRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long endTime;
	@NotEmpty
	private String symbol;
	private String exemptAcct;

	public PostTPlusSellFilterRequest(long endTime, @NotEmpty String symbol) {
		this.endTime = endTime;
		this.symbol = symbol;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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