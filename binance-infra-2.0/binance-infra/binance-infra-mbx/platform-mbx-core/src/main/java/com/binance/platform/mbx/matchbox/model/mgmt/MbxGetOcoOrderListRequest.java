package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * Retrieving a Single OCO
 *
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetOcoOrderListRequest extends MbxBaseRequest {

    @Override
    public String getUri() {
        return "/v1/orderList";
    }

    private long accountId;
    private Long orderListId;
    private String origClientOrderId;

    public MbxGetOcoOrderListRequest(long accountId) {
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
