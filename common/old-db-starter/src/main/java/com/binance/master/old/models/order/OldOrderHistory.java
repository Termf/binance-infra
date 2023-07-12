package com.binance.master.old.models.order;

import java.math.BigDecimal;
import java.util.Date;

public class OldOrderHistory {
    private Long id;

    private String userId;

    private Long orderId;

    private String symbol;

    private Long accountId;

    private String origClientOrderId;

    private String clientOrderId;

    private BigDecimal price;

    private BigDecimal origQty;

    private BigDecimal executedQty;

    private BigDecimal executedQuoteQty;

    private String status;

    private String timeInForce;

    private String type;

    private String side;

    private BigDecimal stopPrice;

    private Date updateTime;

    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? null : symbol.trim();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public void setOrigClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId == null ? null : origClientOrderId.trim();
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId == null ? null : clientOrderId.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOrigQty() {
        return origQty;
    }

    public void setOrigQty(BigDecimal origQty) {
        this.origQty = origQty;
    }

    public BigDecimal getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(BigDecimal executedQty) {
        this.executedQty = executedQty;
    }

    public BigDecimal getExecutedQuoteQty() {
        return executedQuoteQty;
    }

    public void setExecutedQuoteQty(BigDecimal executedQuoteQty) {
        this.executedQuoteQty = executedQuoteQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce == null ? null : timeInForce.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side == null ? null : side.trim();
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
