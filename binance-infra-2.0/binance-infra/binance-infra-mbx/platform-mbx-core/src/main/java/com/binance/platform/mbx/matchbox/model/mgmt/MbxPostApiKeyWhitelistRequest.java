package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPostApiKeyWhitelistRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/apiKey/whitelist";
    }
    private long keyId;
    private long accountId;
    private String symbols;

    public MbxPostApiKeyWhitelistRequest(long keyId, long accountId, String symbols) {
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

    public String getSymbols() {
        return symbols;
    }
}
