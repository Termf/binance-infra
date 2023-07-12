package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetUserTradesRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	private Long endTime;
	private Long fromId;
	private Long limit;
	private Long startTime;
	private String symbol;

	public GetUserTradesRequest(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getFromId() {
		return fromId;
	}

	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}