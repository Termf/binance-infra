package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

public class GetUserUrlReqeust extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = -6104418125176682407L;

    /** 是否后端调用 */
    @NotNull
    private Boolean backend;

    public GetUserUrlReqeust(@NotNull Boolean backend) {
        this.backend = backend;
    }

    public Boolean getBackend() {
        return backend;
    }

    public void setBackend(Boolean backend) {
        this.backend = backend;
    }
}
