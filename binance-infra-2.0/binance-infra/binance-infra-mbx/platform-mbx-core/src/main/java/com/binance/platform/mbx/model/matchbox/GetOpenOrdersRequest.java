package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

public class GetOpenOrdersRequest extends ToString {

    private static final long serialVersionUID = 1L;

    private long accountId;
    private String symbol;

    public GetOpenOrdersRequest(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}