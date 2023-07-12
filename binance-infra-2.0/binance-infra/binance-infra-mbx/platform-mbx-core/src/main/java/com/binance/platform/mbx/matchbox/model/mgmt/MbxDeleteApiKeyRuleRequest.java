package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/14 2:01 下午
 */
public class MbxDeleteApiKeyRuleRequest extends MbxBaseRequest {
    private long ruleId;
    private long keyId;
    private long accountId;

    public MbxDeleteApiKeyRuleRequest(long ruleId, long keyId, long accountId) {
        this.ruleId = ruleId;
        this.keyId = keyId;
        this.accountId = accountId;
    }

    @Override
    public String getUri() {
        return "/v1/apiKey/rule";
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
