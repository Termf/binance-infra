package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class GetAccountFromEngineV3Request extends ToString {

    private static final long serialVersionUID = 8061658217992044677L;

    private long userId;

    /**
     * 
     * @param userId userId, not accountId in matchbox
     */
    public GetAccountFromEngineV3Request(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}