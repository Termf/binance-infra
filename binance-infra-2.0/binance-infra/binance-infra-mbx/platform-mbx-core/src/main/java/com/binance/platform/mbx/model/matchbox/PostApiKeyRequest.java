package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PostApiKeyRequest extends ToString {

    private static final long serialVersionUID = 1L;
    private long accountId;
    @NotEmpty
    private String desc;
    private Boolean canAccessSecureWs;
    private Boolean canControlUserStreams;
    private Boolean canTrade;
    private Boolean canViewMarketData;
    private Boolean canViewUserData;
    private Boolean force;

    private String publicKey;

    public PostApiKeyRequest(long accountId, @NotEmpty String desc) {
        this.accountId = accountId;
        this.desc = desc;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getCanAccessSecureWs() {
        return canAccessSecureWs;
    }

    public void setCanAccessSecureWs(Boolean canAccessSecureWs) {
        this.canAccessSecureWs = canAccessSecureWs;
    }

    public Boolean getCanControlUserStreams() {
        return canControlUserStreams;
    }

    public void setCanControlUserStreams(Boolean canControlUserStreams) {
        this.canControlUserStreams = canControlUserStreams;
    }

    public Boolean getCanTrade() {
        return canTrade;
    }

    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    public Boolean getCanViewMarketData() {
        return canViewMarketData;
    }

    public void setCanViewMarketData(Boolean canViewMarketData) {
        this.canViewMarketData = canViewMarketData;
    }

    public Boolean getCanViewUserData() {
        return canViewUserData;
    }

    public void setCanViewUserData(Boolean canViewUserData) {
        this.canViewUserData = canViewUserData;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}