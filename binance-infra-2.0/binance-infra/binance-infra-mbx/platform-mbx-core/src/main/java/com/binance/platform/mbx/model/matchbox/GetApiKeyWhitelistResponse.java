package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 2:54 上午
 */
public class GetApiKeyWhitelistResponse extends ToString {

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
    private List<String> symbols;
    private Long lastUpdateTime;

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

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
