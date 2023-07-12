package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseModel;

import java.math.BigDecimal;

/**
 * @author Li Fenggang
 * Date: 2020/7/17 3:56 下午
 */
public class RestGetTickerPriceV3Response extends MbxBaseModel {

    /**
     * symbol : BTCUSDT
     * price : 9100.00000000
     */

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
