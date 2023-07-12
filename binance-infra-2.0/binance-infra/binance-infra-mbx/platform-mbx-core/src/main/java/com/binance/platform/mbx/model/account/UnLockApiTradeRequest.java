package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class UnLockApiTradeRequest extends ToString {

    private static final long serialVersionUID = 3254801137013523059L;
    private long userId;

    public UnLockApiTradeRequest(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}