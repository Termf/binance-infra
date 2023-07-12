package com.binance.master.old.models.report;

import java.math.BigDecimal;
import java.util.Date;

public class MarketMonitorModel {
    private String market;
    private long orderNum24H;
    private long orderNum48H;
    private long tradeNum24H;
    private long tradeNum48H;
    private BigDecimal marketQty24H;
    private BigDecimal marketQty48H;
    private BigDecimal marketAmount24H;
    private BigDecimal marketAmount48H;
    private BigDecimal marketQtyReally24H;
    private BigDecimal marketQtyReally48H;
    private BigDecimal marketAmountReally24H;
    private BigDecimal marketAmountReally48H;
    private Date updateTime;



    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public long getOrderNum24H() {
        return orderNum24H;
    }

    public void setOrderNum24H(long orderNum24H) {
        this.orderNum24H = orderNum24H;
    }

    public long getOrderNum48H() {
        return orderNum48H;
    }

    public void setOrderNum48H(long orderNum48H) {
        this.orderNum48H = orderNum48H;
    }

    public long getTradeNum24H() {
        return tradeNum24H;
    }

    public void setTradeNum24H(long tradeNum24H) {
        this.tradeNum24H = tradeNum24H;
    }

    public long getTradeNum48H() {
        return tradeNum48H;
    }

    public void setTradeNum48H(long tradeNum48H) {
        this.tradeNum48H = tradeNum48H;
    }

    public BigDecimal getMarketQty24H() {
        return marketQty24H;
    }

    public void setMarketQty24H(BigDecimal marketQty24H) {
        this.marketQty24H = marketQty24H;
    }

    public BigDecimal getMarketQty48H() {
        return marketQty48H;
    }

    public void setMarketQty48H(BigDecimal marketQty48H) {
        this.marketQty48H = marketQty48H;
    }

    public BigDecimal getMarketAmount24H() {
        return marketAmount24H;
    }

    public void setMarketAmount24H(BigDecimal marketAmount24H) {
        this.marketAmount24H = marketAmount24H;
    }

    public BigDecimal getMarketAmount48H() {
        return marketAmount48H;
    }

    public void setMarketAmount48H(BigDecimal marketAmount48H) {
        this.marketAmount48H = marketAmount48H;
    }

    public BigDecimal getMarketQtyReally24H() {
        return marketQtyReally24H;
    }

    public void setMarketQtyReally24H(BigDecimal marketQtyReally24H) {
        this.marketQtyReally24H = marketQtyReally24H;
    }

    public BigDecimal getMarketQtyReally48H() {
        return marketQtyReally48H;
    }

    public void setMarketQtyReally48H(BigDecimal marketQtyReally48H) {
        this.marketQtyReally48H = marketQtyReally48H;
    }

    public BigDecimal getMarketAmountReally24H() {
        return marketAmountReally24H;
    }

    public void setMarketAmountReally24H(BigDecimal marketAmountReally24H) {
        this.marketAmountReally24H = marketAmountReally24H;
    }

    public BigDecimal getMarketAmountReally48H() {
        return marketAmountReally48H;
    }

    public void setMarketAmountReally48H(BigDecimal marketAmountReally48H) {
        this.marketAmountReally48H = marketAmountReally48H;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
