package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

public class MbxPutAccountCommissionRequest extends MbxBaseRequest {
	@Override
	public String getUri() {
		return "/v1/account/commission";
	}

	private long accountId;
	private long buyerCommission;
	private long makerCommission;
	private long sellerCommission;
	private long takerCommission;

	public MbxPutAccountCommissionRequest(long accountId, long buyerCommission, long makerCommission,
										  long sellerCommission, long takerCommission) {
		this.accountId = accountId;
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