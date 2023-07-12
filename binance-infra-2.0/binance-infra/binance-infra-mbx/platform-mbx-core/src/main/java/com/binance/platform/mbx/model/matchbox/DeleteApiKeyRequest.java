package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class DeleteApiKeyRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private long keyId;

    public DeleteApiKeyRequest(long accountId, long keyId) {
        this.accountId = accountId;
        this.keyId = keyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }
}