package com.binance.platform.mbx.model.traderule;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

/**
 * Data comes from the database(pnk) and table(trading_rule)
 */
public class RefreshTradingRuleRequest extends ToString {

    private static final long serialVersionUID = 4190198297461154862L;

    @NotNull
    private Long ruleId;
    private Double positionLimit;// 最大持仓 //null
    private Double minTrade;// 最小交易 //null
    private Double maxTrade;// 最大交易单位 //null
    private Double tickSize;// 最小价格波动
//    private Double upLimit;
    private Double minPrice; //null
    private Double maxPrice; //null
    private Double stepSize; //null
    private Double minNotional; //null
//    private Double downLimit;
//    private Integer tPlusN;
//    private String description;
    private Double positionLimitValue;//null
    private Double maxTradeValue; //null

    public RefreshTradingRuleRequest(@NotNull Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Double getPositionLimit() {
        return positionLimit;
    }

    public void setPositionLimit(Double positionLimit) {
        this.positionLimit = positionLimit;
    }

    public Double getMinTrade() {
        return minTrade;
    }

    public void setMinTrade(Double minTrade) {
        this.minTrade = minTrade;
    }

    public Double getMaxTrade() {
        return maxTrade;
    }

    public void setMaxTrade(Double maxTrade) {
        this.maxTrade = maxTrade;
    }

    public Double getTickSize() {
        return tickSize;
    }

    public void setTickSize(Double tickSize) {
        this.tickSize = tickSize;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getStepSize() {
        return stepSize;
    }

    public void setStepSize(Double stepSize) {
        this.stepSize = stepSize;
    }

    public Double getMinNotional() {
        return minNotional;
    }

    public void setMinNotional(Double minNotional) {
        this.minNotional = minNotional;
    }

    public Double getPositionLimitValue() {
        return positionLimitValue;
    }

    public void setPositionLimitValue(Double positionLimitValue) {
        this.positionLimitValue = positionLimitValue;
    }

    public Double getMaxTradeValue() {
        return maxTradeValue;
    }

    public void setMaxTradeValue(Double maxTradeValue) {
        this.maxTradeValue = maxTradeValue;
    }
}
