package com.binance.platform.mbx.model.account;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Li Fenggang
 * Date: 2020/8/3 2:34 下午
 */
public class CheckIfBalanceDoneInMbxByTransIdRequest {

    private long userId;
    @NotEmpty
    private String asset;
    @NotNull
    private Long transId;

    /**
     * Construct a request.
     *
     * @param userId
     * @param asset Not empty.
     * @param transId Not empty.
     */
    public CheckIfBalanceDoneInMbxByTransIdRequest(long userId, @NotEmpty String asset, @NotEmpty Long transId) {
        this.userId = userId;
        this.asset = asset;
        this.transId = transId;
    }

    public long getUserId() {
        return userId;
    }

    public String getAsset() {
        return asset;
    }

    public Long getTransId() {
        return transId;
    }
}
