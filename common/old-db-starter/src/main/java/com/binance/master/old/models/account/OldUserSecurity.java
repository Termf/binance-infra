package com.binance.master.old.models.account;

import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUserSecurity extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 2338653555125143949L;

    private Integer id;

    private String userId;

    private Date lastLoginTime;

    private Integer loginFailedNum;

    private Date loginFailedTime;

    private String cashPassword;

    private Integer payPwdFailedNum;

    private Date disableTime;

    private String userType;

    private String status;

    private String moneyPassswordCode;

    private String moneyPassword;

    private Integer moneyPasswordStatus;

    private String secretKey;

    private Integer mobileSecurity;

    private Integer validationStatus;

    private Date unbindTime;

    private Boolean confirmTips;

    private Boolean withdrawWhiteStatus;

    private String encryptedSecretKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginFailedNum() {
        return loginFailedNum;
    }

    public void setLoginFailedNum(Integer loginFailedNum) {
        this.loginFailedNum = loginFailedNum;
    }

    public Date getLoginFailedTime() {
        return loginFailedTime;
    }

    public void setLoginFailedTime(Date loginFailedTime) {
        this.loginFailedTime = loginFailedTime;
    }

    public String getCashPassword() {
        return cashPassword;
    }

    public void setCashPassword(String cashPassword) {
        this.cashPassword = cashPassword == null ? null : cashPassword.trim();
    }

    public Integer getPayPwdFailedNum() {
        return payPwdFailedNum;
    }

    public void setPayPwdFailedNum(Integer payPwdFailedNum) {
        this.payPwdFailedNum = payPwdFailedNum;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getMoneyPassswordCode() {
        return moneyPassswordCode;
    }

    public void setMoneyPassswordCode(String moneyPassswordCode) {
        this.moneyPassswordCode = moneyPassswordCode == null ? null : moneyPassswordCode.trim();
    }

    public String getMoneyPassword() {
        return moneyPassword;
    }

    public void setMoneyPassword(String moneyPassword) {
        this.moneyPassword = moneyPassword == null ? null : moneyPassword.trim();
    }

    public Integer getMoneyPasswordStatus() {
        return moneyPasswordStatus;
    }

    public void setMoneyPasswordStatus(Integer moneyPasswordStatus) {
        this.moneyPasswordStatus = moneyPasswordStatus;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }

    public Integer getMobileSecurity() {
        return mobileSecurity;
    }

    public void setMobileSecurity(Integer mobileSecurity) {
        this.mobileSecurity = mobileSecurity;
    }

    public Integer getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(Integer validationStatus) {
        this.validationStatus = validationStatus;
    }

    public Date getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(Date unbindTime) {
        this.unbindTime = unbindTime;
    }

    public Boolean getConfirmTips() {
        return confirmTips;
    }

    public void setConfirmTips(Boolean confirmTips) {
        this.confirmTips = confirmTips;
    }

    public Boolean getWithdrawWhiteStatus() {
        return withdrawWhiteStatus;
    }

    public void setWithdrawWhiteStatus(Boolean withdrawWhiteStatus) {
        this.withdrawWhiteStatus = withdrawWhiteStatus;
    }

    public String getEncryptedSecretKey() {
        return encryptedSecretKey;
    }

    public void setEncryptedSecretKey(String encryptedSecretKey) {
        this.encryptedSecretKey = encryptedSecretKey;
    }

}
