package com.binance.master.old.models.trade;

import java.math.BigDecimal;
import java.util.Date;

public class TradeItem {
    private Long id;
    private Long tradeId;
    private Long buyerOrderId;
    private String buyerUserId;
    private Long sellerOrderId;
    private String sellerUserId;
    private BigDecimal buyerFee;
    private BigDecimal sellerFee;
    private BigDecimal price;
    private BigDecimal quantity;
    private Date time;
    private String symbol;
    private String side;
    private Boolean activeBuy;
    private BigDecimal realPnl;
    private String feeAsset;
    private String buyCommissionAsset;
    private String sellCommissionAsset;
    private BigDecimal totalPrice;
    private String userId;

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(Long buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public Long getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(Long sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public BigDecimal getBuyerFee() {
        return buyerFee;
    }

    public void setBuyerFee(BigDecimal buyerFee) {
        this.buyerFee = buyerFee;
    }

    public BigDecimal getSellerFee() {
        return sellerFee;
    }

    public void setSellerFee(BigDecimal sellerFee) {
        this.sellerFee = sellerFee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Boolean getActiveBuy() {
        return activeBuy;
    }

    public void setActiveBuy(Boolean activeBuy) {
        this.activeBuy = activeBuy;
    }

    public BigDecimal getRealPnl() {
        return realPnl;
    }

    public void setRealPnl(BigDecimal realPnl) {
        this.realPnl = realPnl;
    }

    public String getFeeAsset() {
        return feeAsset;
    }

    public void setFeeAsset(String feeAsset) {
        this.feeAsset = feeAsset;
    }

    public String getBuyCommissionAsset() {
        return buyCommissionAsset;
    }

    public void setBuyCommissionAsset(String buyCommissionAsset) {
        this.buyCommissionAsset = buyCommissionAsset;
    }

    public String getSellCommissionAsset() {
        return sellCommissionAsset;
    }

    public void setSellCommissionAsset(String sellCommissionAsset) {
        this.sellCommissionAsset = sellCommissionAsset;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
