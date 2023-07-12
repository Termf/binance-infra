package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostOcoOrderRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/order/oco";
    }

    private long accountId;

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

    public MbxPostOcoOrderRequest(long accountId, String symbol, String side, String quantity, String price, String stopPrice) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.stopPrice = stopPrice;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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
