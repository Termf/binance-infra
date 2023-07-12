package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

public class GetProductByRuleIdRequest extends ToString {
    private static final long serialVersionUID = -5657287021546835719L;
    @NotNull
    private Long ruleId;

    public GetProductByRuleIdRequest() {
    }

    public Long getRuleId() {
        return this.ruleId;
    }

    public void setRuleId(final Long ruleId) {
        this.ruleId = ruleId;
    }
}