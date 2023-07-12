package com.binance.master.old.models.product;

import java.math.BigDecimal;
import java.util.Date;

public class UserProductData {
    private String userId;

    private String symbol;

    private Boolean fullDepth;

    private Date updateTime;

    private BigDecimal buyerCommission;

    private BigDecimal sellerCommission;

    private BigDecimal makerCommission;

    private BigDecimal takerCommission;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public Boolean getFullDepth() {
        return fullDepth;
    }

    public void setFullDepth(Boolean fullDepth) {
        this.fullDepth = fullDepth;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
}