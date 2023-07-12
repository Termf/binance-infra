package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.util.List;

/**
 * Creating an account (v3)
 */
public class PostAccountRequestV3 extends ToString {

    private long externalAccountId;
    private int permissionsBitmask;
    private Long buyerCommission;
    private Long makerCommission;
    private Long sellerCommission;
    private Long takerCommission;
    private Boolean canDeposit;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private String accountType;
    private String restrictionMode;
    private List<String> symbols;

    public PostAccountRequestV3(long externalAccountId, int permissionsBitmask) {
        this.externalAccountId = externalAccountId;
        this.permissionsBitmask = permissionsBitmask;
    }

    public long getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(long externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public int getPermissionsBitmask() {
        return permissionsBitmask;
    }

    public void setPermissionsBitmask(int permissionsBitmask) {
        this.permissionsBitmask = permissionsBitmask;
    }

    public Long getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Long buyerCommission) {
        this.buyerCommission = buyerCommission;
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

    public Boolean getCanDeposit() {
        return canDeposit;
    }

    /** If not set, default value is True */
    public void setCanDeposit(Boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    public Boolean getCanTrade() {
        return canTrade;
    }

    /** If not set, default value is True */
    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    public Boolean getCanWithdraw() {
        return canWithdraw;
    }

    /** If not set, default value is True */
    public void setCanWithdraw(Boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * Default value is SPOT.<br/>
     * Valid values are SPOT, MARGIN,C2C, ISOLATED_MARGIN, ZERO and MP
     * @param accountType
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRestrictionMode() {
        return restrictionMode;
    }

    public void setRestrictionMode(String restrictionMode) {
        this.restrictionMode = restrictionMode;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

}