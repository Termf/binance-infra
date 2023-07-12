package com.binance.master.old.models.trade;

import java.math.BigDecimal;
import java.util.Date;

public class OldTradeHistory {
    private Long id;

    private Long tradeId;

    private String symbol;

    private String buyerUserId;

    private Long buyerOrderId;

    private String buyCommissionAsset;

    private String sellerUserId;

    private Long sellerOrderId;

    private String sellCommissionAsset;

    private BigDecimal buyerFee;

    private BigDecimal sellerFee;

    private BigDecimal price;

    private BigDecimal qty;

    private Boolean activeBuy;

    private BigDecimal realPnl;

    private Date time;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId == null ? null : buyerUserId.trim();
    }

    public Long getBuyerOrderId() {
        return buyerOrderId;
    }

    public void setBuyerOrderId(Long buyerOrderId) {
        this.buyerOrderId = buyerOrderId;
    }

    public String getBuyCommissionAsset() {
        return buyCommissionAsset;
    }

    public void setBuyCommissionAsset(String buyCommissionAsset) {
        this.buyCommissionAsset = buyCommissionAsset == null ? null : buyCommissionAsset.trim();
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId == null ? null : sellerUserId.trim();
    }

    public Long getSellerOrderId() {
        return sellerOrderId;
    }

    public void setSellerOrderId(Long sellerOrderId) {
        this.sellerOrderId = sellerOrderId;
    }

    public String getSellCommissionAsset() {
        return sellCommissionAsset;
    }

    public void setSellCommissionAsset(String sellCommissionAsset) {
        this.sellCommissionAsset = sellCommissionAsset == null ? null : sellCommissionAsset.trim();
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
