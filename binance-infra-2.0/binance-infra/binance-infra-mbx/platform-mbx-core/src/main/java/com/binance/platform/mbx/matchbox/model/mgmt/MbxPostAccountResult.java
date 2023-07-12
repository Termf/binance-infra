package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

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
public class MbxPostAccountResult extends MbxBaseModel {

    private Long accountId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
