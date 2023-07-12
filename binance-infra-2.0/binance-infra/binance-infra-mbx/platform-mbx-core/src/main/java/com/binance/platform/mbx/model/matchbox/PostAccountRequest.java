package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class PostAccountRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long externalId;
    private Long buyerCommission;
    private Boolean canDeposit;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Long makerCommission;
    private Long sellerCommission;
    private Long takerCommission;
    private String accountType;

    public PostAccountRequest(long externalId) {
        this.externalId = externalId;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public Long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public Boolean getCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(Boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public Boolean getCanTrade() {
        return canTrade;
    }

    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    public Boolean getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(Boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public Long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(Long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public Long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(Long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public Long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(Long takerCommission) {
        this.takerCommission = takerCommission;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * values: SPOT and MARGIN.
     * accountType defaults to SPOT.
     * accountType cannot be changed once set on an account.
     * Accounts with the MARGIN accountType can only trade on symbols that allow margin trading.
     * <p>
     * accountType is included on all OutboundAccountInfo events from the private streamer.
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}