package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class SetGasRequest extends ToString {

    private static final long serialVersionUID = 8061658217992044677L;

    private long userId;
    private boolean useBnbFee;

    public SetGasRequest(long userId, boolean useBnbFee) {
        this.userId = userId;
        this.useBnbFee = useBnbFee;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isUseBnbFee() {
        return useBnbFee;
    }

    public void setUseBnbFee(boolean useBnbFee) {
        this.useBnbFee = useBnbFee;
    }
}