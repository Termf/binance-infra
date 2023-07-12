package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.annotation.MbxField;
import com.binance.platform.mbx.matchbox.annotation.MbxIgnored;
import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;
import com.binance.platform.mbx.util.ArrayValueUtil;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostAccountV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/account";
    }

    private long externalAccountId;
    private int permissionsBitmask;
    private Boolean canDeposit;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Long buyerCommission;
    private Long makerCommission;
    private Long sellerCommission;
    private Long takerCommission;
    private String accountType;
    private String restrictionMode;
    private List<String> symbols;

    public MbxPostAccountV3Request(long externalAccountId, int permissionsBitmask) {
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

    public String getRestrictionMode() {
        return restrictionMode;
    }

    public void setRestrictionMode(String restrictionMode) {
        this.restrictionMode = restrictionMode;
    }

    @MbxIgnored
    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    @MbxField("symbols")
    public String getActualSymbols() {
        return ArrayValueUtil.convert(symbols);
    }
}
