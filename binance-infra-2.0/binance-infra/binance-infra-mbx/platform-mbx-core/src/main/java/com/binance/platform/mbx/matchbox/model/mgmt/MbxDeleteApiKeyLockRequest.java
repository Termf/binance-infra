package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * Deleting a lock on the account
 *
 * @author Li Fenggang
 * Date: 2020/7/14 7:31 上午
 */
public class MbxDeleteApiKeyLockRequest extends MbxBaseRequest {
    private long accountId;
    private boolean revert;

    public MbxDeleteApiKeyLockRequest(long accountId, boolean revert) {
        this.accountId = accountId;
        this.revert = revert;
    }

    @Override
    public String getUri() {
        return "/v1/apiKey/lock";
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public boolean isRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }
}
