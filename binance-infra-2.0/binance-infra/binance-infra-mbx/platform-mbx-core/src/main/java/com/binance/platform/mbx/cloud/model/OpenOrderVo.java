package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class OpenOrderVo extends ToString {
    private static final long serialVersionUID = 8629545085230105837L;

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "订单号")
    private Long orderId;

    @ApiModelProperty(name = "交易账号")
    private Long accountId;

    @ApiModelProperty(name = "用户ID")
    private String userId;

    @ApiModelProperty(name = "产品代码")
    private String symbol;

    @ApiModelProperty(name = "客户单号")
    private String clientOrderId;

    @ApiModelProperty(name = "原始客户单号")
    private String origClientOrderId;

    @ApiModelProperty(name = "价格")
    private String price;

    @ApiModelProperty(name = "平均价格")
    private String avgPrice;

    @ApiModelProperty(name = "下单数量")
    private String origQty;

    @ApiModelProperty(name = "qty")
    private BigDecimal qty;

    @ApiModelProperty(name = "money")
    private BigDecimal money;

    @ApiModelProperty(name = "成交数量")
    private String executedQty;

    @ApiModelProperty(name = "已成交金额")
    private String executedQuoteQty;

    @ApiModelProperty(name = "tradedVolume")
    private BigDecimal tradedVolume;

    @ApiModelProperty(name = "状态(NEW: 未成交, PARTIALLY_FILLED: 部分成交, FILLED: 全部成交, CANCELLED: 已撤销)")
    private String status;

    @ApiModelProperty(name = "订单生存周期")
    private String timeInForce;

    @ApiModelProperty(name = "订单类型")
    private String type;

    @ApiModelProperty(name = "买卖方向")
    private String side;

    @ApiModelProperty(name = "突破价格")
    private BigDecimal stopPrice;

    @ApiModelProperty(name = "下单时间")
    private Long time;

    @ApiModelProperty(name = "订单更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "基础资产")
    private String baseAsset;

    @ApiModelProperty(name = "标价货币")
    private String quoteAsset;

    @ApiModelProperty(name = "delegateMoney")
    private String delegateMoney;

    @ApiModelProperty(name = "executedPrice")
    private String executedPrice;

    @ApiModelProperty(name = "productName")
    private String productName;

    @ApiModelProperty(name = "matchingUnitType")
    private String matchingUnitType;

    @ApiModelProperty(name = "订单类型")
    private String orderType;

    @ApiModelProperty(name = "order list id")
    private Long orderListId;

    @ApiModelProperty(name = "msg auth type")
    private String msgAuth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public void setOrigClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getOrigQty() {
        return origQty;
    }

    public void setOrigQty(String origQty) {
        this.origQty = origQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(String executedQty) {
        this.executedQty = executedQty;
    }

    public String getExecutedQuoteQty() {
        return executedQuoteQty;
    }

    public void setExecutedQuoteQty(String executedQuoteQty) {
        this.executedQuoteQty = executedQuoteQty;
    }

    public BigDecimal getTradedVolume() {
        return tradedVolume;
    }

    public void setTradedVolume(BigDecimal tradedVolume) {
        this.tradedVolume = tradedVolume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public String getDelegateMoney() {
        return delegateMoney;
    }

    public void setDelegateMoney(String delegateMoney) {
        this.delegateMoney = delegateMoney;
    }

    public String getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(String executedPrice) {
        this.executedPrice = executedPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMatchingUnitType() {
        return matchingUnitType;
    }

    public void setMatchingUnitType(String matchingUnitType) {
        this.matchingUnitType = matchingUnitType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(Long orderListId) {
        this.orderListId = orderListId;
    }

    public String getMsgAuth() {
        return msgAuth;
    }

    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }
}