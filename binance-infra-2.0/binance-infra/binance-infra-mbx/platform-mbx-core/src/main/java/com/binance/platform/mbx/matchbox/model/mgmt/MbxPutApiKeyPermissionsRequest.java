package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutApiKeyPermissionsRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/apiKey/permissions";
    }

    private long accountId;
    private long keyId;
    private boolean canAccessSecureWs;
    private boolean canControlUserStreams;
    private boolean canTrade;
    private boolean canViewMarketData;
    private boolean canViewUserData;
    private Boolean force;

    public MbxPutApiKeyPermissionsRequest(long accountId, long keyId, boolean canAccessSecureWs,
                                          boolean canControlUserStreams, boolean canTrade, boolean canViewMarketData,
                                          boolean canViewUserData) {
        this.accountId = accountId;
        this.keyId = keyId;
        this.canAccessSecureWs = canAccessSecureWs;
        this.canControlUserStreams = canControlUserStreams;
        this.canTrade = canTrade;
        this.canViewMarketData = canViewMarketData;
        this.canViewUserData = canViewUserData;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public boolean isCanAccessSecureWs() {
        return canAccessSecureWs;
    }

    public void setCanAccessSecureWs(boolean canAccessSecureWs) {
        this.canAccessSecureWs = canAccessSecureWs;
    }

    public boolean isCanControlUserStreams() {
        return canControlUserStreams;
    }

    public void setCanControlUserStreams(boolean canControlUserStreams) {
        this.canControlUserStreams = canControlUserStreams;
    }

    public boolean isCanTrade() {
        return canTrade;
    }

    public void setCanTrade(boolean canTrade) {
        this.canTrade = canTrade;
    }

    public boolean isCanViewMarketData() {
        return canViewMarketData;
    }

    public void setCanViewMarketData(boolean canViewMarketData) {
        this.canViewMarketData = canViewMarketData;
    }

    public boolean isCanViewUserData() {
        return canViewUserData;
    }

    public void setCanViewUserData(boolean canViewUserData) {
        this.canViewUserData = canViewUserData;
    }

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }
}
