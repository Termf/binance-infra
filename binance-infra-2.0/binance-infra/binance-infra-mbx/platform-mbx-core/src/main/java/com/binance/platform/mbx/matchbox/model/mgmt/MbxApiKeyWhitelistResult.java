package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.util.List;

/**
 *
 * keyId : 251
 * accountId : 5
 * type : HMAC_SHA256
 * desc : test
 * permissions : ["USER_DATA","USER_STREAM","MARKET_DATA","SECURE_WEB_SOCKET"]
 * rules : [{"ruleId":276,"ip":"0.0.0.0","effectiveFrom":1593589593391}]
 *
 * @author Li Fenggang
 * Date: 2020/7/2 10:53 上午
 */
public class MbxApiKeyWhitelistResult extends MbxBaseModel {

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
