package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class PutAccountSymbolsCommissionRequest extends ToString {

	private static final long serialVersionUID = 1L;

	private long accountId;
	@NotEmpty
	private List<String> symbols;
	private long buyerCommission;
	private long makerCommission;
	private long sellerCommission;
	private long takerCommission;

	public PutAccountSymbolsCommissionRequest(long accountId, @NotEmpty List<String> symbols, long buyerCommission,
											  long makerCommission, long sellerCommission, long takerCommission) {
		this.accountId = accountId;
		this.symbols = symbols;
		this.buyerCommission = buyerCommission;
		this.makerCommission = makerCommission;
		this.sellerCommission = sellerCommission;
		this.takerCommission = takerCommission;
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

	public long getBuyerCommission() {
		return buyerCommission;
	}

	public void setBuyerCommission(long buyerCommission) {
		this.buyerCommission = buyerCommission;
	}

	public long getMakerCommission() {
		return makerCommission;
	}

	public void setMakerCommission(long makerCommission) {
		this.makerCommission = makerCommission;
	}

	public long getSellerCommission() {
		return sellerCommission;
	}

	public void setSellerCommission(long sellerCommission) {
		this.sellerCommission = sellerCommission;
	}

	public long getTakerCommission() {
		return takerCommission;
	}

	public void setTakerCommission(long takerCommission) {
		this.takerCommission = takerCommission;
	}
}