package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostApiKeyRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/apiKey";
    }
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

    public MbxPostApiKeyRequest(long accountId, @NotEmpty String desc) {
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
