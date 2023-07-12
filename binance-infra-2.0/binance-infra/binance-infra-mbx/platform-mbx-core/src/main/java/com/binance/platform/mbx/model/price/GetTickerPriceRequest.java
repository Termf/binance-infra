package com.binance.platform.mbx.model.price;

import com.binance.master.commons.ToString;

/**
 * @author wangxiao
 */
public class GetTickerPriceRequest extends ToString {
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
