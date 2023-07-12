package com.binance.platform.mbx.model.userstream;


import com.binance.master.commons.ToString;

public class StartStreamRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;

    public StartStreamRequest(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

}
