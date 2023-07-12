package com.binance.platform.mbx.model.userstream;


import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class DeleteStreamRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;

    // 非必传为了临时兼容旧版，mbx升级完成后应该必传
    //@NotEmpty
    private long userId;

    @NotEmpty
    private String listenKey;

    public DeleteStreamRequest(long userId, @NotEmpty String listenKey) {
        this.userId = userId;
        this.listenKey = listenKey;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }
}
