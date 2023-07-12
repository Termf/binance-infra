package com.binance.master.old.models.account;

import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUserLog extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 2379584703137927279L;

    private Long id;

    private String userId;

    private Date loginTime;

    private String loginResult;

    private String ipAddress;

    private String ipLocation;

    private String resInfo;

    private String clientType;

    private String versionCode;

    private String browser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult == null ? null : loginResult.trim();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    public String getIpLocation() {
        return ipLocation;
    }

    public void setIpLocation(String ipLocation) {
        this.ipLocation = ipLocation == null ? null : ipLocation.trim();
    }

    public String getResInfo() {
        return resInfo;
    }

    public void setResInfo(String resInfo) {
        this.resInfo = resInfo == null ? null : resInfo.trim();
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType == null ? null : clientType.trim();
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode == null ? null : versionCode.trim();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }
}
