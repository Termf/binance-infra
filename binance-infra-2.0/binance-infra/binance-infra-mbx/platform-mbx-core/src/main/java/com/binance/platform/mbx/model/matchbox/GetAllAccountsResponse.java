package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 11:55 下午
 */
public class GetAllAccountsResponse extends ToString {

    /**
     * accountId : 0
     * externalId : 350788190
     * externalAccountId : 350788190
     * canTrade : true
     * canWithdraw : true
     * canDeposit : true
     * accountType : C2C
     */

    private Long accountId;
    private Long externalId;
    private Long externalAccountId;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private String accountType;

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
