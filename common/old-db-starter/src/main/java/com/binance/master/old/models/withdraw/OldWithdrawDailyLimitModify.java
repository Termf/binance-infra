package com.binance.master.old.models.withdraw;

import java.math.BigDecimal;
import java.util.Date;

public class OldWithdrawDailyLimitModify {
    private String userId;

    private BigDecimal withdrawDaliyLimitLast;

    private String modifyCause;

    private Short autoRestore;

    private Date restoreTimePlan;

    private Date restoreTimeActual;

    private String forbidReason;

    private Date forbidRestoreTime;

    private Date forbidRestoreTimeActual;

    private Short forbidAutoRestore;

    private String applyInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public BigDecimal getWithdrawDaliyLimitLast() {
        return withdrawDaliyLimitLast;
    }

    public void setWithdrawDaliyLimitLast(BigDecimal withdrawDaliyLimitLast) {
        this.withdrawDaliyLimitLast = withdrawDaliyLimitLast;
    }

    public String getModifyCause() {
        return modifyCause;
    }

    public void setModifyCause(String modifyCause) {
        this.modifyCause = modifyCause == null ? null : modifyCause.trim();
    }

    public Short getAutoRestore() {
        return autoRestore;
    }

    public void setAutoRestore(Short autoRestore) {
        this.autoRestore = autoRestore;
    }

    public Date getRestoreTimePlan() {
        return restoreTimePlan;
    }

    public void setRestoreTimePlan(Date restoreTimePlan) {
        this.restoreTimePlan = restoreTimePlan;
    }

    public Date getRestoreTimeActual() {
        return restoreTimeActual;
    }

    public void setRestoreTimeActual(Date restoreTimeActual) {
        this.restoreTimeActual = restoreTimeActual;
    }

    public String getForbidReason() {
        return forbidReason;
    }

    public void setForbidReason(String forbidReason) {
        this.forbidReason = forbidReason == null ? null : forbidReason.trim();
    }

    public Date getForbidRestoreTime() {
        return forbidRestoreTime;
    }

    public void setForbidRestoreTime(Date forbidRestoreTime) {
        this.forbidRestoreTime = forbidRestoreTime;
    }

    public Date getForbidRestoreTimeActual() {
        return forbidRestoreTimeActual;
    }

    public void setForbidRestoreTimeActual(Date forbidRestoreTimeActual) {
        this.forbidRestoreTimeActual = forbidRestoreTimeActual;
    }

    public Short getForbidAutoRestore() {
        return forbidAutoRestore;
    }

    public void setForbidAutoRestore(Short forbidAutoRestore) {
        this.forbidAutoRestore = forbidAutoRestore;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo == null ? null : applyInfo.trim();
    }
}