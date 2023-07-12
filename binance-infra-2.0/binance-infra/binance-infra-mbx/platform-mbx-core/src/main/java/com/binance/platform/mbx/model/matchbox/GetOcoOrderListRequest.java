package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetOcoOrderListRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private Long orderListId;
    private String origClientOrderId;

    public GetOcoOrderListRequest(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Long getOrderListId() {
        return orderListId;
    }

    public void setOrderListId(Long orderListId) {
        this.orderListId = orderListId;
    }

    public String getOrigClientOrderId() {
        return origClientOrderId;
    }

    public void setOrigClientOrderId(String origClientOrderId) {
        this.origClientOrderId = origClientOrderId;
    }
}