package com.binance.platform.mbx.cloud.model;

import com.binance.master.utils.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 6980434842040150016L;
    private Long userId;
    private Long parent;
    private Long agentId;
    private BigDecimal agentRewardRatio;
    private BigDecimal referralRewardRatio;
    private Long tradingAccount;
    private BigDecimal makerCommission;
    private BigDecimal takerCommission;
    private BigDecimal buyerCommission;
    private BigDecimal sellerCommission;
    private BigDecimal dailyWithdrawCap;
    private Integer dailyWithdrawCountLimit;
    private BigDecimal autoWithdrawAuditThreshold;
    private String nickName;
    private String remark;
    private String trackSource;
    private Date updateTime;
    private Date insertTime;
    private Integer tradeLevel;
    private Long marginUserId;
    private Long fiatUserId;
    private Long futureUserId;
    private Long meTradingAccount;
    private BigDecimal trade30btcNumber;
    private String tradeAutoStatus;
    private BigDecimal dailyFiatWithdrawCap;
    private BigDecimal singleFiatWithdrawCap;
    private Long miningUserId;
    private Long deliveryTradingAccount;
    private Long cardUserId;

    public UserInfoVo() {
    }

    public String toString() {
        return StringUtils.objectToString(this);
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getParent() {
        return this.parent;
    }

    public Long getAgentId() {
        return this.agentId;
    }

    public BigDecimal getAgentRewardRatio() {
        return this.agentRewardRatio;
    }

    public BigDecimal getReferralRewardRatio() {
        return this.referralRewardRatio;
    }

    public Long getTradingAccount() {
        return this.tradingAccount;
    }

    public BigDecimal getMakerCommission() {
        return this.makerCommission;
    }

    public BigDecimal getTakerCommission() {
        return this.takerCommission;
    }

    public BigDecimal getBuyerCommission() {
        return this.buyerCommission;
    }

    public BigDecimal getSellerCommission() {
        return this.sellerCommission;
    }

    public BigDecimal getDailyWithdrawCap() {
        return this.dailyWithdrawCap;
    }

    public Integer getDailyWithdrawCountLimit() {
        return this.dailyWithdrawCountLimit;
    }

    public BigDecimal getAutoWithdrawAuditThreshold() {
        return this.autoWithdrawAuditThreshold;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getTrackSource() {
        return this.trackSource;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Date getInsertTime() {
        return this.insertTime;
    }

    public Integer getTradeLevel() {
        return this.tradeLevel;
    }

    public Long getMarginUserId() {
        return this.marginUserId;
    }

    public Long getFiatUserId() {
        return this.fiatUserId;
    }

    public Long getFutureUserId() {
        return this.futureUserId;
    }

    public Long getMeTradingAccount() {
        return this.meTradingAccount;
    }

    public BigDecimal getTrade30btcNumber() {
        return this.trade30btcNumber;
    }

    public String getTradeAutoStatus() {
        return this.tradeAutoStatus;
    }

    public BigDecimal getDailyFiatWithdrawCap() {
        return this.dailyFiatWithdrawCap;
    }

    public BigDecimal getSingleFiatWithdrawCap() {
        return this.singleFiatWithdrawCap;
    }

    public Long getMiningUserId() {
        return this.miningUserId;
    }

    public Long getDeliveryTradingAccount() {
        return this.deliveryTradingAccount;
    }

    public Long getCardUserId() {
        return this.cardUserId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setParent(final Long parent) {
        this.parent = parent;
    }

    public void setAgentId(final Long agentId) {
        this.agentId = agentId;
    }

    public void setAgentRewardRatio(final BigDecimal agentRewardRatio) {
        this.agentRewardRatio = agentRewardRatio;
    }

    public void setReferralRewardRatio(final BigDecimal referralRewardRatio) {
        this.referralRewardRatio = referralRewardRatio;
    }

    public void setTradingAccount(final Long tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public void setMakerCommission(final BigDecimal makerCommission) {
        this.makerCommission = makerCommission;
    }

    public void setTakerCommission(final BigDecimal takerCommission) {
        this.takerCommission = takerCommission;
    }

    public void setBuyerCommission(final BigDecimal buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public void setSellerCommission(final BigDecimal sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    public void setDailyWithdrawCap(final BigDecimal dailyWithdrawCap) {
        this.dailyWithdrawCap = dailyWithdrawCap;
    }

    public void setDailyWithdrawCountLimit(final Integer dailyWithdrawCountLimit) {
        this.dailyWithdrawCountLimit = dailyWithdrawCountLimit;
    }

    public void setAutoWithdrawAuditThreshold(final BigDecimal autoWithdrawAuditThreshold) {
        this.autoWithdrawAuditThreshold = autoWithdrawAuditThreshold;
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public void setRemark(final String remark) {
        this.remark = remark;
    }

    public void setTrackSource(final String trackSource) {
        this.trackSource = trackSource;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setInsertTime(final Date insertTime) {
        this.insertTime = insertTime;
    }

    public void setTradeLevel(final Integer tradeLevel) {
        this.tradeLevel = tradeLevel;
    }

    public void setMarginUserId(final Long marginUserId) {
        this.marginUserId = marginUserId;
    }

    public void setFiatUserId(final Long fiatUserId) {
        this.fiatUserId = fiatUserId;
    }

    public void setFutureUserId(final Long futureUserId) {
        this.futureUserId = futureUserId;
    }

    public void setMeTradingAccount(final Long meTradingAccount) {
        this.meTradingAccount = meTradingAccount;
    }

    public void setTrade30btcNumber(final BigDecimal trade30btcNumber) {
        this.trade30btcNumber = trade30btcNumber;
    }

    public void setTradeAutoStatus(final String tradeAutoStatus) {
        this.tradeAutoStatus = tradeAutoStatus;
    }

    public void setDailyFiatWithdrawCap(final BigDecimal dailyFiatWithdrawCap) {
        this.dailyFiatWithdrawCap = dailyFiatWithdrawCap;
    }

    public void setSingleFiatWithdrawCap(final BigDecimal singleFiatWithdrawCap) {
        this.singleFiatWithdrawCap = singleFiatWithdrawCap;
    }

    public void setMiningUserId(final Long miningUserId) {
        this.miningUserId = miningUserId;
    }

    public void setDeliveryTradingAccount(final Long deliveryTradingAccount) {
        this.deliveryTradingAccount = deliveryTradingAccount;
    }

    public void setCardUserId(final Long cardUserId) {
        this.cardUserId = cardUserId;
    }
}