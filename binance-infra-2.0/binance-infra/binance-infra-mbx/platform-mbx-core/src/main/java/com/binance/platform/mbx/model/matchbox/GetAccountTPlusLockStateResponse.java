package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import java.util.List;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:17 上午
 */
public class GetAccountTPlusLockStateResponse extends ToString {

    /**
     * accountId : 0
     * symbol : ABCDEF
     * asset : ABC
     * unlockData : []
     */

    private Long accountId;
    private String symbol;
    private String asset;
    private List<?> unlockData;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public List<?> getUnlockData() {
        return unlockData;
    }

    public void setUnlockData(List<?> unlockData) {
        this.unlockData = unlockData;
    }
}
