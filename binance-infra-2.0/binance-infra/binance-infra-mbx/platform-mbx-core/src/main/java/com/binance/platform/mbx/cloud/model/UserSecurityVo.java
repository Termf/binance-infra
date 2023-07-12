package com.binance.platform.mbx.cloud.model;

import com.binance.master.utils.StringUtils;
import com.binance.platform.mbx.cloud.enums.ProtectedStatus;

import java.io.Serializable;
import java.util.Date;

public class UserSecurityVo implements Serializable {
    private static final long serialVersionUID = -6383538130583482368L;
    private Long userId;
    private String email;
    private String antiPhishingCode;
    private Integer securityLevel;
    private String mobileCode;
    private String mobile;
    private Integer loginFailedNum;
    private Date loginFailedTime;
    private String authKey;
    private Date disableTime;
    private Date unbindTime;
    private Date deregisterYubikeyTime;
    private Date lastLoginTime;
    private Date lockEndTime;
    private Date insertTime;
    private Date updateTime;
    private Integer withdrawSecurityStatus;
    private Integer withdrawSecurityAutoStatus;
    private Integer withdrawSecurityFaceStatus;
    private ProtectedStatus protectedStatus;

    public UserSecurityVo() {
    }

    public String toString() {
        return StringUtils.objectToString(this);
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAntiPhishingCode() {
        return this.antiPhishingCode;
    }

    public Integer getSecurityLevel() {
        return this.securityLevel;
    }

    public String getMobileCode() {
        return this.mobileCode;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getLoginFailedNum() {
        return this.loginFailedNum;
    }

    public Date getLoginFailedTime() {
        return this.loginFailedTime;
    }

    public String getAuthKey() {
        return this.authKey;
    }

    public Date getDisableTime() {
        return this.disableTime;
    }

    public Date getUnbindTime() {
        return this.unbindTime;
    }

    public Date getDeregisterYubikeyTime() {
        return this.deregisterYubikeyTime;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public Date getLockEndTime() {
        return this.lockEndTime;
    }

    public Date getInsertTime() {
        return this.insertTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Integer getWithdrawSecurityStatus() {
        return this.withdrawSecurityStatus;
    }

    public Integer getWithdrawSecurityAutoStatus() {
        return this.withdrawSecurityAutoStatus;
    }

    public Integer getWithdrawSecurityFaceStatus() {
        return this.withdrawSecurityFaceStatus;
    }

    public ProtectedStatus getProtectedStatus() {
        return this.protectedStatus;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setAntiPhishingCode(final String antiPhishingCode) {
        this.antiPhishingCode = antiPhishingCode;
    }

    public void setSecurityLevel(final Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public void setMobileCode(final String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    public void setLoginFailedNum(final Integer loginFailedNum) {
        this.loginFailedNum = loginFailedNum;
    }

    public void setLoginFailedTime(final Date loginFailedTime) {
        this.loginFailedTime = loginFailedTime;
    }

    public void setAuthKey(final String authKey) {
        this.authKey = authKey;
    }

    public void setDisableTime(final Date disableTime) {
        this.disableTime = disableTime;
    }

    public void setUnbindTime(final Date unbindTime) {
        this.unbindTime = unbindTime;
    }

    public void setDeregisterYubikeyTime(final Date deregisterYubikeyTime) {
        this.deregisterYubikeyTime = deregisterYubikeyTime;
    }

    public void setLastLoginTime(final Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLockEndTime(final Date lockEndTime) {
        this.lockEndTime = lockEndTime;
    }

    public void setInsertTime(final Date insertTime) {
        this.insertTime = insertTime;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setWithdrawSecurityStatus(final Integer withdrawSecurityStatus) {
        this.withdrawSecurityStatus = withdrawSecurityStatus;
    }

    public void setWithdrawSecurityAutoStatus(final Integer withdrawSecurityAutoStatus) {
        this.withdrawSecurityAutoStatus = withdrawSecurityAutoStatus;
    }

    public void setWithdrawSecurityFaceStatus(final Integer withdrawSecurityFaceStatus) {
        this.withdrawSecurityFaceStatus = withdrawSecurityFaceStatus;
    }

    public void setProtectedStatus(final ProtectedStatus protectedStatus) {
        this.protectedStatus = protectedStatus;
    }
}