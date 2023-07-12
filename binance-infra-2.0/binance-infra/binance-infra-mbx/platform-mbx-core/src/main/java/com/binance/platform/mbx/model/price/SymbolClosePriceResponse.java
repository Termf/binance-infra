package com.binance.platform.mbx.model.price;

import com.binance.master.commons.ToString;

import java.math.BigDecimal;

/**
 * 交易对最新价格
 */
public class SymbolClosePriceResponse extends ToString {
        private String symbol;
        private BigDecimal price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
