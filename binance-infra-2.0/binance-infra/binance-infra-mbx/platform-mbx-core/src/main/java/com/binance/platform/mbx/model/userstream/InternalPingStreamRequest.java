package com.binance.platform.mbx.model.userstream;


import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class InternalPingStreamRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    @NotEmpty
    private String listenKey;

    // 非必传为了临时兼容旧版，mbx升级完成后应该必传
    //@ApiModelProperty(required = true)
    //@NotEmpty
    private long userId;

    public InternalPingStreamRequest(@NotEmpty String listenKey, long userId) {
        this.listenKey = listenKey;
        this.userId = userId;
    }

    public String getListenKey() {
        return listenKey;
    }

    public void setListenKey(String listenKey) {
        this.listenKey = listenKey;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
