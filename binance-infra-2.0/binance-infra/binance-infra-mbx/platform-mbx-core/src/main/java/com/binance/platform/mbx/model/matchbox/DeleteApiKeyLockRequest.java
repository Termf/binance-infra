package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class DeleteApiKeyLockRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private boolean revert;

    public DeleteApiKeyLockRequest(long accountId, boolean revert) {
        this.accountId = accountId;
        this.revert = revert;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public boolean getRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }
}