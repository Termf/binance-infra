package com.binance.platform.mbx.model.order;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 下单对象. <br/>
 * 除了同于注解标识的非空属性外，限价单时属性price不能为空；止盈止损单时price和stopPrice不能为空。
 */
public class PlaceOrderRequest extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = 1519729030268737898L;

    private long userId;
    @NotNull
    private String symbol;
    @NotEmpty
    private String side;
    @NotEmpty
    private String type;
    // quantity 和 quoteOrderQty 必传一个
    private Double quantity;
    private Double price;
    private Double stopPrice;
    private Long orderId;
    private Boolean active;
    private String timeInForce;
    private String msgAuth;
    private String newClientOrderId;
    private Double icebergQty;
    private String newOrderRespType;
    private Double quoteOrderQty;

    public PlaceOrderRequest(long userId, @NotNull String symbol, @NotEmpty String side, @NotEmpty String type) {
        this.userId = userId;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(Double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getMsgAuth() {
        return msgAuth;
    }

    /**
     * values:
     * NORMAL
     * LEGACY_FORCE
     * LIQUIDATION
     * MANUAL
     * When msgAuth is not sent, it defaults to NORMAL.
     * When the msgAuth is set to anything other than NORMAL, the order or cancel will break tradingPhase, accountType, and symbol permission rules.
     * When the msgAuth is set to LIQUIDATION or MANUAL, those orders can be queried using the msgAuthOrders endpoint.
     * NORMAL and LEGACY_FORCE orders are not specially tracked.
     * MANUAL is intended to be used by the Core Tech team if needed in emergency situations. Please do not use msgAuth=MANUAL.
     * msgAuth is also included now in all executionReports coming from the private streamer.
     * The msgAuth for an exeuctionReport is determined by the parent order, so if an order is placed with msgAuth=LIQUIDATION, all trade executionReports from that order will have msgAuth=LIQUIDATION.
     */
    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }

    public String getNewClientOrderId() {
        return newClientOrderId;
    }

    public void setNewClientOrderId(String newClientOrderId) {
        this.newClientOrderId = newClientOrderId;
    }

    public Double getIcebergQty() {
        return icebergQty;
    }

    public void setIcebergQty(Double icebergQty) {
        this.icebergQty = icebergQty;
    }

    public String getNewOrderRespType() {
        return newOrderRespType;
    }

    public void setNewOrderRespType(String newOrderRespType) {
        this.newOrderRespType = newOrderRespType;
    }

    public Double getQuoteOrderQty() {
        return quoteOrderQty;
    }

    /**
     * “报价总额市价单” 允许用户在市价单MARKET中设置总的购买投入金额或卖出预计回收金额 quoteOrderQty。
     * “报价总额市价单”不会突破LOT_SIZE的限制规则; 报单会按给定的quoteOrderQty尽可能接近地被执行。
     * 以BNBBTC交易对为例:
     * On the BUY side, the order will buy as many BNB as quoteOrderQty BTC can.
     * 买单: 给定quoteOrderQty的BTC会被用来市价买入尽可能多的BNB。
     * On the SELL side, the order will sell as much BNB as needed to receive quoteOrderQty BTC.
     * 卖单: 持有BNB会被尽可能多地以市价卖出以获取给定quoteOrderQty的BTC。
     */
    public void setQuoteOrderQty(Double quoteOrderQty) {
        this.quoteOrderQty = quoteOrderQty;
    }
}
