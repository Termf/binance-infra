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
public class MbxPutAccountV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/account";
    }

    private long accountId;
    private int permissionsBitmask;
    private Long makerCommission;
    private Long takerCommission;
    private Long buyerCommission;
    private Long sellerCommission;
    private String externalAccountId;
    private Boolean canDeposit;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private String accountType;
    private String restrictionMode;
    private List<String> symbols;

    public MbxPutAccountV3Request(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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

    public String getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(String externalAccountId) {
        this.externalAccountId = externalAccountId;
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

    @MbxField("symbols")
    public String getActualSymbols() {
        return ArrayValueUtil.convert(symbols);
    }

    @MbxIgnored
    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public int getPermissionsBitmask() {
        return permissionsBitmask;
    }

    /**
     * Valid range: has to be greater than -2147483648 and less than 2147483647 (Signed 32 bit integer)
     * Currently there are only 3 permissions:
     * Bit(0) = allowSpot
     * Bit(1) = allowMargin
     * Bit(2) = leverage token
     */
    public void setPermissionsBitmask(int permissionsBitmask) {
        this.permissionsBitmask = permissionsBitmask;
    }
}
