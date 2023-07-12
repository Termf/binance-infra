package com.binance.master.old.models.account;

import java.math.BigDecimal;

import com.binance.master.commons.ToString;

public class OldSecurityPolicy extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = -8398915474792332670L;
    private Integer id;
    // 安全级别
    private Integer securityLevel;
    // 审核额度
    private BigDecimal reviewQuota;
    // 盈亏阈值
    private BigDecimal pnlThreshold;
    // 提现额度
    private BigDecimal withdraw;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
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

    public BigDecimal getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(BigDecimal withdraw) {
        this.withdraw = withdraw;
    }


}
