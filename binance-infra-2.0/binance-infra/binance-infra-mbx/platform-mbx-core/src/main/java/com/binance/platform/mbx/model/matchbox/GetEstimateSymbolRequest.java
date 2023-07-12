package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class GetEstimateSymbolRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String baseDecimalPlaces;
	@NotEmpty
	private String quoteDecimalPlaces;
	private String mathSystemType;
	private String maxPrice;
	private String maxQty;

	public GetEstimateSymbolRequest(@NotEmpty String baseDecimalPlaces, @NotEmpty String quoteDecimalPlaces) {
		this.baseDecimalPlaces = baseDecimalPlaces;
		this.quoteDecimalPlaces = quoteDecimalPlaces;
	}

	public String getBaseDecimalPlaces() {
		return baseDecimalPlaces;
	}

	public void setBaseDecimalPlaces(String baseDecimalPlaces) {
		this.baseDecimalPlaces = baseDecimalPlaces;
	}

	public String getQuoteDecimalPlaces() {
		return quoteDecimalPlaces;
	}

	public void setQuoteDecimalPlaces(String quoteDecimalPlaces) {
		this.quoteDecimalPlaces = quoteDecimalPlaces;
	}

	public String getMathSystemType() {
		return mathSystemType;
	}

	public void setMathSystemType(String mathSystemType) {
		this.mathSystemType = mathSystemType;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
	}
}