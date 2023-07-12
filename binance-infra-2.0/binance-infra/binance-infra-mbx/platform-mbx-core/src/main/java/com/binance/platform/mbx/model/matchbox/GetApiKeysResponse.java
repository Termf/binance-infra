package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 2:54 上午
 */
public class GetApiKeysResponse extends ToString {

    /**
     * keyId : 56
     * accountId : 6
     * type : HMAC_SHA256
     * desc : test
     * permissions : ["USER_DATA","USER_STREAM","MARKET_DATA","SECURE_WEB_SOCKET"]
     * rules : [{"ruleId":87,"ip":"0.0.0.0","effectiveFrom":1595117006462},{"ruleId":88,"ip":"192.168.1.1","effectiveFrom":1595119828011},{"ruleId":105,"ip":"192.168.1.2","effectiveFrom":1595180767880}]
     */

    private Long keyId;
    private Long accountId;
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public static class Rule implements Serializable {
        /**
         * ruleId : 87
         * ip : 0.0.0.0
         * effectiveFrom : 1595117006462
         */

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
