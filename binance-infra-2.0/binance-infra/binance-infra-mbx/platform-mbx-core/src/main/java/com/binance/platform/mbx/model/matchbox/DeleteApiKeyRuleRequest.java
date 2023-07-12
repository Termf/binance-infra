package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class DeleteApiKeyRuleRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private long keyId;
    private long ruleId;

    public DeleteApiKeyRuleRequest(long accountId, long keyId, long ruleId) {
        this.accountId = accountId;
        this.keyId = keyId;
        this.ruleId = ruleId;
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

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }
}