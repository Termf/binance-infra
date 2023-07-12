package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * orderId and origClientOrderId, at least one of them is required.
 */
public class GetOrderRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;
    private Long orderId;
    private String origClientOrderId;

    /**
     * orderId and origClientOrderId, at least one of them is required.
     */
    public GetOrderRequest(String symbol) {
        this.symbol = symbol;
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

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public void setOrigClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId;
    }
}