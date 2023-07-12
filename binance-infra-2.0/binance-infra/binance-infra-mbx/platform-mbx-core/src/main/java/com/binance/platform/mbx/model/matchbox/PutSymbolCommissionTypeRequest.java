package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutSymbolCommissionTypeRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String commissionType;
    @NotEmpty
    private String symbol;

	public PutSymbolCommissionTypeRequest(@NotEmpty String commissionType, @NotEmpty String symbol) {
		this.commissionType = commissionType;
		this.symbol = symbol;
	}

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}