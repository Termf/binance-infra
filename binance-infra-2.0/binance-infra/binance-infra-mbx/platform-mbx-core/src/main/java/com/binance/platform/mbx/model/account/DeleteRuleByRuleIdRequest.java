package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

public class DeleteRuleByRuleIdRequest extends ToString {

    private static final long serialVersionUID = -4445310547066405042L;
    private long userId;
    private long keyId;
    private long ruleId;

    public DeleteRuleByRuleIdRequest(long userId, long keyId, long ruleId) {
        this.userId = userId;
        this.keyId = keyId;
        this.ruleId = ruleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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