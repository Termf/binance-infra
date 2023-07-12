package com.binance.master.old.models.account;

import java.math.BigDecimal;
import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUserData extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 5394535953323100428L;

    private String userId;

    private Boolean active;

    private Boolean purchase;

    private Boolean trade;

    private Boolean changePass;

    private Boolean fullDepth;

    private String agentComment;

    private String comment;

    private Date activateTime;

    private Date updateTime;

    private Byte children;

    private Boolean protocolConfirm;

    private String userType;

    private String unionId;

    private Byte wxBindStatus;

    private Integer advanceOrderLimit;

    private BigDecimal withdrawMaxAssetDay;

    private Integer withdrawMaxCountDay;

    private Integer securityLevel;

    private Integer afsSecurityLevel;

    private Integer afsSecurityScore;

    private Integer commissionStatus;

    private Boolean specialFlag;

    private Integer seedUser;

    private BigDecimal reviewQuota;

    private BigDecimal pnlThreshold;

    private Integer canTrade;

    private Byte tradeForbidden;

    private Byte appTrade;

    private String antiPhishingCode;

    private Boolean apiTradeStatus;

    private String mobileCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPurchase() {
        return purchase;
    }

    public void setPurchase(Boolean purchase) {
        this.purchase = purchase;
    }

    public Boolean getTrade() {
        return trade;
    }

    public void setTrade(Boolean trade) {
        this.trade = trade;
    }

    public Boolean getChangePass() {
        return changePass;
    }

    public void setChangePass(Boolean changePass) {
        this.changePass = changePass;
    }

    public Boolean getFullDepth() {
        return fullDepth;
    }

    public void setFullDepth(Boolean fullDepth) {
        this.fullDepth = fullDepth;
    }

    public String getAgentComment() {
        return agentComment;
    }

    public void setAgentComment(String agentComment) {
        this.agentComment = agentComment == null ? null : agentComment.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getChildren() {
        return children;
    }

    public void setChildren(Byte children) {
        this.children = children;
    }

    public Boolean getProtocolConfirm() {
        return protocolConfirm;
    }

    public void setProtocolConfirm(Boolean protocolConfirm) {
        this.protocolConfirm = protocolConfirm;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId == null ? null : unionId.trim();
    }

    public Byte getWxBindStatus() {
        return wxBindStatus;
    }

    public void setWxBindStatus(Byte wxBindStatus) {
        this.wxBindStatus = wxBindStatus;
    }

    public Integer getAdvanceOrderLimit() {
        return advanceOrderLimit;
    }

    public void setAdvanceOrderLimit(Integer advanceOrderLimit) {
        this.advanceOrderLimit = advanceOrderLimit;
    }

    public BigDecimal getWithdrawMaxAssetDay() {
        return withdrawMaxAssetDay;
    }

    public void setWithdrawMaxAssetDay(BigDecimal withdrawMaxAssetDay) {
        this.withdrawMaxAssetDay = withdrawMaxAssetDay;
    }

    public Integer getWithdrawMaxCountDay() {
        return withdrawMaxCountDay;
    }

    public void setWithdrawMaxCountDay(Integer withdrawMaxCountDay) {
        this.withdrawMaxCountDay = withdrawMaxCountDay;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Integer getAfsSecurityLevel() {
        return afsSecurityLevel;
    }

    public void setAfsSecurityLevel(Integer afsSecurityLevel) {
        this.afsSecurityLevel = afsSecurityLevel;
    }

    public Integer getAfsSecurityScore() {
        return afsSecurityScore;
    }

    public void setAfsSecurityScore(Integer afsSecurityScore) {
        this.afsSecurityScore = afsSecurityScore;
    }

    public Integer getCommissionStatus() {
        return commissionStatus;
    }

    public void setCommissionStatus(Integer commissionStatus) {
        this.commissionStatus = commissionStatus;
    }

    public Boolean getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(Boolean specialFlag) {
        this.specialFlag = specialFlag;
    }

    public Integer getSeedUser() {
        return seedUser;
    }

    public void setSeedUser(Integer seedUser) {
        this.seedUser = seedUser;
    }

    public BigDecimal getReviewQuota() {
        return reviewQuota;
    }

    public void setReviewQuota(BigDecimal reviewQuota) {
        this.reviewQuota = reviewQuota;
    }

    public BigDecimal getPnlThreshold() {
        return pnlThreshold;
    }

    public void setPnlThreshold(BigDecimal pnlThreshold) {
        this.pnlThreshold = pnlThreshold;
    }

    public Integer getCanTrade() {
        return canTrade;
    }

    public void setCanTrade(Integer canTrade) {
        this.canTrade = canTrade;
    }

    public Byte getTradeForbidden() {
        return tradeForbidden;
    }

    public void setTradeForbidden(Byte tradeForbidden) {
        this.tradeForbidden = tradeForbidden;
    }

    public Byte getAppTrade() {
        return appTrade;
    }

    public void setAppTrade(Byte appTrade) {
        this.appTrade = appTrade;
    }

    public String getAntiPhishingCode() {
        return antiPhishingCode;
    }

    public void setAntiPhishingCode(String antiPhishingCode) {
        this.antiPhishingCode = antiPhishingCode == null ? null : antiPhishingCode.trim();
    }

    public Boolean getApiTradeStatus() {
        return apiTradeStatus;
    }

    public void setApiTradeStatus(Boolean apiTradeStatus) {
        this.apiTradeStatus = apiTradeStatus;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode == null ? null : mobileCode.trim();
    }
}
