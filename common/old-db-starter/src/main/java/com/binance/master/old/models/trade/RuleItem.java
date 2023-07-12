package com.binance.master.old.models.trade;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.List;

public class RuleItem {
    private Long id;
    private Double positionLimit;// 最大持仓
    private Double minTrade;// 最小交易
    private Double maxTrade;// 最大交易单位
    private Double tickSize;// 最小价格波动
    private Double upLimit;
    private Double minPrice;
    private Double maxPrice;
    private Double stepSize;
    private Double minNotional;
    private Double downLimit;
    private Integer tPlusN;
    private String description;
    private Double positionLimitValue;
    private Double maxTradeValue;

    // trades those filled the order
    private List<RuleDetailItem> details = Lists.newLinkedList();

    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("id", id).add("positionLimit", positionLimit)
                .add("minTrade", minTrade).add("maxTrade", maxTrade).add("tickSize", tickSize).add("uplimit", upLimit)
                .add("downLimit", downLimit).add("description", description).add("details", details).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(Double upLimit) {
        this.upLimit = upLimit;
    }

    public Double getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(Double downLimit) {
        this.downLimit = downLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RuleDetailItem> getDetails() {
        return details;
    }

    public void setDetails(List<RuleDetailItem> details) {
        this.details = details;
    }

    public Integer gettPlusN() {
        return tPlusN;
    }

    public void settPlusN(Integer tPlusN) {
        this.tPlusN = tPlusN;
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

}
