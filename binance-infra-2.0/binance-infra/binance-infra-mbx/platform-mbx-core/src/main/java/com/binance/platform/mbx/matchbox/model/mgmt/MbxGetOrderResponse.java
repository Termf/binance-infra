package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 1:39 上午
 */
public class MbxGetOrderResponse extends MbxBaseModel {

    /**
     * accountId : 2172
     * symbol : BTCUSDT
     * orderId : 282
     * orderListId : -1
     * clientOrderId : WWUFKj01kzgviKUhHIlIcd
     * price : 0.00000000
     * origQty : 2.00000000
     * executedQty : 2.00000000
     * cummulativeQuoteQty : 18000.00000000
     * status : FILLED
     * timeInForce : GTC
     * type : MARKET
     * side : SELL
     * stopPrice : 0.00000000
     * icebergQty : 0.00000000
     * time : 1595227994150
     * updateTime : 1595227994150
     * isWorking : true
     * origQuoteOrderQty : 0.00000000
     */

    private Long accountId;
    private String symbol;
    private Long orderId;
    private Long orderListId;
    private String clientOrderId;
    private BigDecimal price;
    private BigDecimal origQty;
    private BigDecimal executedQty;
    private BigDecimal cummulativeQuoteQty;
    private String status;
    private String timeInForce;
    private String type;
    private String side;
    private BigDecimal stopPrice;
    private BigDecimal icebergQty;
    private Long time;
    private Long updateTime;
    private Boolean isWorking;
    private BigDecimal origQuoteOrderQty;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(Long orderListId) {
        this.orderListId = orderListId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
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

    public BigDecimal getCummulativeQuoteQty() {
        return cummulativeQuoteQty;
    }

    public void setCummulativeQuoteQty(BigDecimal cummulativeQuoteQty) {
        this.cummulativeQuoteQty = cummulativeQuoteQty;
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

    public BigDecimal getIcebergQty() {
        return icebergQty;
    }

    public void setIcebergQty(BigDecimal icebergQty) {
        this.icebergQty = icebergQty;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean isIsWorking() {
        return isWorking;
    }

    public void setIsWorking(Boolean isWorking) {
        this.isWorking = isWorking;
    }

    public BigDecimal getOrigQuoteOrderQty() {
        return origQuoteOrderQty;
    }

    public void setOrigQuoteOrderQty(BigDecimal origQuoteOrderQty) {
        this.origQuoteOrderQty = origQuoteOrderQty;
    }
}
