package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 1:32 下午
 */
public class MbxPostAccountRequest extends MbxBaseRequest {
    private Long makerCommission;
    private Long takerCommission;
    private Long buyerCommission;
    private Long sellerCommission;
    private long externalId;
    /** If not set, default value is True */
    private Boolean canTrade;
    /** If not set, default value is True */
    private Boolean canWithdraw;
    /** If not set, default value is True */
    private Boolean canDeposit;
    /** Default value is SPOT.
     * Valid values are SPOT, MARGIN,C2C, ISOLATED_MARGIN, ZERO and MP */
    private String accountType;

    public MbxPostAccountRequest(long externalId) {
        this.externalId = externalId;
    }

    @Override
    public String getUri() {
        return "/v1/account";
    }

    public Long getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(Long makerCommission) {
        this.makerCommission = makerCommission;
    }

    public Long getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(Long takerCommission) {
        this.takerCommission = takerCommission;
    }

    public Long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Long buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public Long getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(Long sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
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

    public Boolean getCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(Boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
