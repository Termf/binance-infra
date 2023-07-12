package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:12 上午
 */
public class PostAccountResponseV3 extends ToString {

    /**
     * makerCommission : 10
     * takerCommission : 11
     * buyerCommission : 12
     * sellerCommission : 13
     * canTrade : true
     * canWithdraw : true
     * canDeposit : true
     * accountId : 21457
     * externalId : 163
     * externalAccountId : 163
     * updateTime : 1596558593176
     * updateId : 31123
     * accountType : SPOT
     * balances : [{"asset":"BNB","free":"0.00000000","locked":"0.00000000","extLocked":"0.00000000"}]
     * restrictions : {"restrictionId":9,"mode":"WHITE_LIST","symbols":["BTCUSDT"]}
     * permissions : ["UNKNOWN_1"]
     */

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
    private Long updateTime;
    private Long updateId;
    private String accountType;
    private Restrictions restrictions;
    private List<Balance> balances;
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

    public Boolean isCanTrade() {
        return canTrade;
    }

    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    public Boolean isCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(Boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public Boolean isCanDeposit() {
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class Restrictions implements Serializable {
        /**
         * restrictionId : 9
         * mode : WHITE_LIST
         * symbols : ["BTCUSDT"]
         */

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

    public static class Balance implements Serializable {
        /**
         * asset : BNB
         * free : 0.00000000
         * locked : 0.00000000
         * extLocked : 0.00000000
         */

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
}
