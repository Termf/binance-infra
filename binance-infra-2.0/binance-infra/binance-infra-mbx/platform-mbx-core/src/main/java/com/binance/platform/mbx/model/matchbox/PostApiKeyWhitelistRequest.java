package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.util.List;

public class PostApiKeyWhitelistRequest extends ToString {

    private static final long serialVersionUID = 1L;
    private long keyId;
    private long accountId;
    private List<String> symbols;

    public PostApiKeyWhitelistRequest(long keyId, long accountId, List<String> symbols) {
        this.keyId = keyId;
        this.accountId = accountId;
        this.symbols = symbols;
    }

    public long getKeyId() {
        return keyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public List<String> getSymbols() {
        return symbols;
    }
}