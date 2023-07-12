package com.binance.platform.mbx.model.account;

import com.binance.master.commons.ToString;

import java.util.List;

public class ApiKeyInfoVo extends ToString {
    private Long keyId;
    private Long accountId;
    private String desc;
    private List<String> permissions;
    private List<Rule> rules;

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static class Rule extends ToString {
        private Long ruleId;
        private String ip;
        private Long effectiveFrom;

        public Long getRuleId() {
            return ruleId;
        }

        public void setRuleId(Long ruleId) {
            this.ruleId = ruleId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Long getEffectiveFrom() {
            return effectiveFrom;
        }

        public void setEffectiveFrom(Long effectiveFrom) {
            this.effectiveFrom = effectiveFrom;
        }
    }
}