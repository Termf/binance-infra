package com.binance.master.old.models.withdraw;

import java.math.BigDecimal;
import java.util.Date;

public class OldWithdrawExepmtReviewAmount {
    private String userId;

    private BigDecimal amount;

    private String reason;

    private Short autoRestore;

    private Date restorePlanTime;

    private Date restoreActualTime;

    private Short active;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private Date dbCreateTime;

    private Date dbModifyTime;

    private String applyInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Short getAutoRestore() {
        return autoRestore;
    }

    public void setAutoRestore(Short autoRestore) {
        this.autoRestore = autoRestore;
    }

    public Date getRestorePlanTime() {
        return restorePlanTime;
    }

    public void setRestorePlanTime(Date restorePlanTime) {
        this.restorePlanTime = restorePlanTime;
    }

    public Date getRestoreActualTime() {
        return restoreActualTime;
    }

    public void setRestoreActualTime(Date restoreActualTime) {
        this.restoreActualTime = restoreActualTime;
    }

    public Short getActive() {
        return active;
    }

    public void setActive(Short active) {
        this.active = active;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getDbCreateTime() {
        return dbCreateTime;
    }

    public void setDbCreateTime(Date dbCreateTime) {
        this.dbCreateTime = dbCreateTime;
    }

    public Date getDbModifyTime() {
        return dbModifyTime;
    }

    public void setDbModifyTime(Date dbModifyTime) {
        this.dbModifyTime = dbModifyTime;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo == null ? null : applyInfo.trim();
    }
}