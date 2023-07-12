package com.binance.platform.mbx.cloud.model;

import com.binance.master.utils.StringUtils;

import java.io.Serializable;

public class UserIdRequest implements Serializable {
    private static final long serialVersionUID = 4337520945072011095L;
    private Long userId;

    public UserIdRequest() {
    }

    public String toString() {
        return StringUtils.objectToString(this);
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}