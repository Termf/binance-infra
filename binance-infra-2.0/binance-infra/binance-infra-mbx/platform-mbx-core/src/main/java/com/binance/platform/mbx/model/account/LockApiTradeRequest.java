package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;
import io.swagger.annotations.ApiModelProperty;

public class LockApiTradeRequest extends ToString {

    private static final long serialVersionUID = 3254801137013523059L;
    @ApiModelProperty(required = true)
    private long userId;

    public LockApiTradeRequest(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}