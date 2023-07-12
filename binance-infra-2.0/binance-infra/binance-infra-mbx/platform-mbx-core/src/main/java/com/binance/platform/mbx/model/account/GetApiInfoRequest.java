package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class GetApiInfoRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;

    public GetApiInfoRequest(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}