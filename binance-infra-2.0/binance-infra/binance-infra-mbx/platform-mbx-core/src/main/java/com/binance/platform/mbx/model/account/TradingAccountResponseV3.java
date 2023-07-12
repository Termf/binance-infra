package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交易账户信息 V3版接口
 */
public class TradingAccountResponseV3 extends ToString {

    private Long makerCommission;
    private Long takerCommission;
    private Long buyerCommission;
    private Long sellerCommission;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private Long accountId;
    private Long externalId;
    private Long externalAccountId;
    private Long updateId;
    private Date updateTime;
    private String accountType;
    private List<Balance> balances;
    private Restriction restrictions;
    private List<String> permissions;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Long getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(Long externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public Restriction getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restriction restrictions) {
        this.restrictions = restrictions;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class Balance {
        private String asset;
        private BigDecimal free;
        private BigDecimal locked;
        private BigDecimal extLocked;

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public BigDecimal getFree() {
            return free;
        }

        public void setFree(BigDecimal free) {
            this.free = free;
        }

        public BigDecimal getLocked() {
            return locked;
        }

        public void setLocked(BigDecimal locked) {
            this.locked = locked;
        }

        public BigDecimal getExtLocked() {
            return extLocked;
        }

        public void setExtLocked(BigDecimal extLocked) {
            this.extLocked = extLocked;
        }

    }

    public static class Restriction {
        private Long restrictionId;
        private String mode;
        private List<String> symbols;

        public Long getRestrictionId() {
            return restrictionId;
        }

        public void setRestrictionId(Long restrictionId) {
            this.restrictionId = restrictionId;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public List<String> getSymbols() {
            return symbols;
        }

        public void setSymbols(List<String> symbols) {
            this.symbols = symbols;
        }
    }

}