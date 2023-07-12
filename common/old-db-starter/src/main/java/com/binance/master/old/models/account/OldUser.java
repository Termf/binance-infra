package com.binance.master.old.models.account;

import java.math.BigDecimal;
import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUser extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = -2870513107817440535L;

    private String userId;

    private String email;

    private String mobileNo;

    private String parent;

    private String agentId;

    private String realAgent;

    private String password;

    private String salt;

    private String emailVerified;

    private String passwdVerifyCode;

    private String appActiveCode;

    private String emailVerifyCode;

    private Integer agentLevel;

    private String saleman;

    private Integer agentProvince;

    private BigDecimal agentRewardRatio;

    private Long tradingAccount;

    private BigDecimal makerCommission;

    private BigDecimal takerCommission;

    private BigDecimal buyerCommission;

    private BigDecimal sellerCommission;

    private Date updateTime;

    private Date createTime;

    private String nickName;

    private Integer departmentId;

    private String remark;

    private Date pwUpdateTime;

    private String ts;

    private Date emailVerifiedTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent == null ? null : parent.trim();
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getRealAgent() {
        return realAgent;
    }

    public void setRealAgent(String realAgent) {
        this.realAgent = realAgent == null ? null : realAgent.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified == null ? null : emailVerified.trim();
    }

    public String getPasswdVerifyCode() {
        return passwdVerifyCode;
    }

    public void setPasswdVerifyCode(String passwdVerifyCode) {
        this.passwdVerifyCode = passwdVerifyCode == null ? null : passwdVerifyCode.trim();
    }

    public String getAppActiveCode() {
        return appActiveCode;
    }

    public void setAppActiveCode(String appActiveCode) {
        this.appActiveCode = appActiveCode == null ? null : appActiveCode.trim();
    }

    public String getEmailVerifyCode() {
        return emailVerifyCode;
    }

    public void setEmailVerifyCode(String emailVerifyCode) {
        this.emailVerifyCode = emailVerifyCode == null ? null : emailVerifyCode.trim();
    }

    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getSaleman() {
        return saleman;
    }

    public void setSaleman(String saleman) {
        this.saleman = saleman == null ? null : saleman.trim();
    }

    public Integer getAgentProvince() {
        return agentProvince;
    }

    public void setAgentProvince(Integer agentProvince) {
        this.agentProvince = agentProvince;
    }

    public BigDecimal getAgentRewardRatio() {
        return agentRewardRatio;
    }

    public void setAgentRewardRatio(BigDecimal agentRewardRatio) {
        this.agentRewardRatio = agentRewardRatio;
    }

    public Long getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Long tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public BigDecimal getMakerCommission() {
        return makerCommission;
    }

    public void setMakerCommission(BigDecimal makerCommission) {
        this.makerCommission = makerCommission;
    }

    public BigDecimal getTakerCommission() {
        return takerCommission;
    }

    public void setTakerCommission(BigDecimal takerCommission) {
        this.takerCommission = takerCommission;
    }

    public BigDecimal getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(BigDecimal buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public BigDecimal getSellerCommission() {
        return sellerCommission;
    }

    public void setSellerCommission(BigDecimal sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getPwUpdateTime() {
        return pwUpdateTime;
    }

    public void setPwUpdateTime(Date pwUpdateTime) {
        this.pwUpdateTime = pwUpdateTime;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts == null ? null : ts.trim();
    }

    public Date getEmailVerifiedTime() {
        return emailVerifiedTime;
    }

    public void setEmailVerifiedTime(Date emailVerifiedTime) {
        this.emailVerifiedTime = emailVerifiedTime;
    }
}
