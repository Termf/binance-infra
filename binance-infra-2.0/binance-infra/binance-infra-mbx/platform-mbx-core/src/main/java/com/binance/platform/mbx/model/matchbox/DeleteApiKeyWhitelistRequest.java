package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class DeleteApiKeyWhitelistRequest extends ToString {

    private static final long serialVersionUID = 1L;
    private long keyId;
    private long accountId;

    public DeleteApiKeyWhitelistRequest(long keyId, long accountId) {
        this.keyId = keyId;
        this.accountId = accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    public long getAccountId() {
        return accountId;
    }
}