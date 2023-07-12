package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:07 上午
 */
public class PostAccountResponse extends ToString {

    /**
     * accountId : 21453
     */

    private Long accountId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
