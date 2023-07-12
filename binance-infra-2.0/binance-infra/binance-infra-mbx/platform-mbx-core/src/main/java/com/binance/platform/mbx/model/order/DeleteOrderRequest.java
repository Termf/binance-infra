package com.binance.platform.mbx.model.order;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DeleteOrderRequest extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = 702237489734274531L;
    private long userId;
    @NotEmpty
    private List<String> symbols;
    @NotEmpty
    private List<String> orderIds;

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
    private String msgAuth;
    
    private String origClientOrderId;
    
    private String newClientOrderId;

    public DeleteOrderRequest(long userId, List<String> symbols, List<String> orderIds) {
        this.userId = userId;
        this.symbols = symbols;
        this.orderIds = orderIds;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public String getMsgAuth() {
        return msgAuth;
    }

    public void setMsgAuth(String msgAuth) {
        this.msgAuth = msgAuth;
    }

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public void setOrigClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId;
    }

    public String getNewClientOrderId() {
        return newClientOrderId;
    }

    public void setNewClientOrderId(String newClientOrderId) {
        this.newClientOrderId = newClientOrderId;
    }
}