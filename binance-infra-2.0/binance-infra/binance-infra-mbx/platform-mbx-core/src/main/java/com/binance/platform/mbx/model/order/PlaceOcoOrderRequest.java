package com.binance.platform.mbx.model.order;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * oco下单对象
 */
public class PlaceOcoOrderRequest extends ToString {

    private long userId;
    @NotEmpty
    private String symbol;
    @NotEmpty
    private String side;
    @NotEmpty
    private String quantity;
    @NotEmpty
    private String price;
    @NotEmpty
    private String stopPrice;
    private String listClientOrderId;
    private String limitIcebergQty;
    private String stopClientOrderId;
    private String stopLimitPrice;
    private String stopIcebergQty;
    private String stopLimitTimeInForce;
    private String msgAuth;
    private String newOrderRespType;

    public PlaceOcoOrderRequest(long userId, @NotEmpty String symbol, @NotEmpty String side,
                                @NotEmpty String quantity, @NotEmpty String price, @NotEmpty String stopPrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.stopPrice = stopPrice;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getListClientOrderId() {
        return listClientOrderId;
    }

    /**
     * Unique string ID for an orderlist
     */
    public void setListClientOrderId(String listClientOrderId) {
        this.listClientOrderId = listClientOrderId;
    }

    public String getLimitIcebergQty() {
        return limitIcebergQty;
    }

    public void setLimitIcebergQty(String limitIcebergQty) {
        this.limitIcebergQty = limitIcebergQty;
    }

    public String getStopClientOrderId() {
        return stopClientOrderId;
    }

    /**
     * Unique string Id for the stop loss leg
     */
    public void setStopClientOrderId(String stopClientOrderId) {
        this.stopClientOrderId = stopClientOrderId;
    }

    public String getStopLimitPrice() {
        return stopLimitPrice;
    }

    public void setStopLimitPrice(String stopLimitPrice) {
        this.stopLimitPrice = stopLimitPrice;
    }

    public String getStopIcebergQty() {
        return stopIcebergQty;
    }

    public void setStopIcebergQty(String stopIcebergQty) {
        this.stopIcebergQty = stopIcebergQty;
    }

    public String getStopLimitTimeInForce() {
        return stopLimitTimeInForce;
    }

    /**
     * Makes the stop leg a ```STOP_LOSS_LIMIT``` leg. Valid values are ```GTC```/```FOK```/```IOC```
     */
    public void setStopLimitTimeInForce(String stopLimitTimeInForce) {
        this.stopLimitTimeInForce = stopLimitTimeInForce;
    }

    public String getMsgAuth() {
        return msgAuth;
    }
    /**
     *  values:
     *  NORMAL
     *  LEGACY_FORCE
     *  LIQUIDATION
     *  MANUAL
     *  When msgAuth is not sent, it defaults to NORMAL.
     *  When the msgAuth is set to anything other than NORMAL, the order or cancel will break tradingPhase, accountType, and symbol permission rules.
     *  When the msgAuth is set to LIQUIDATION or MANUAL, those orders can be queried using the msgAuthOrders endpoint.
     *  NORMAL and LEGACY_FORCE orders are not specially tracked.
     *  MANUAL is intended to be used by the Core Tech team if needed in emergency situations. Please do not use msgAuth=MANUAL.
     *  msgAuth is also included now in all executionReports coming from the private streamer.
     *  The msgAuth for an exeuctionReport is determined by the parent order, so if an order is placed with msgAuth=LIQUIDATION, all trade executionReports from that order will have msgAuth=LIQUIDATION.
     */
    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }

    public String getNewOrderRespType() {
        return newOrderRespType;
    }

    public void setNewOrderRespType(String newOrderRespType) {
        this.newOrderRespType = newOrderRespType;
    }
}
