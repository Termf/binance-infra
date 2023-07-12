package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GetAccountTPlusLockStateRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	@NotEmpty
	private List<String> symbols;

	public GetAccountTPlusLockStateRequest(long accountId, @NotEmpty List<String> symbols) {
		this.accountId = accountId;
		this.symbols = symbols;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}
}