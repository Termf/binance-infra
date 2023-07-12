package com.binance.platform.mbx.model.product;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 设置下单频率的请求对象
 */
public class NumAlgoOrdersRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = -4427259454824583093L;
    /**
     * 交易对
     */
    @NotEmpty
    private String symbol;
    /**
     * 数量
     */
    @NotNull
    private Integer numOrders;

    public NumAlgoOrdersRequest(@NotEmpty String symbol, @NotNull Integer numOrders) {
        this.symbol = symbol;
        this.numOrders = numOrders;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(Integer numOrders) {
        this.numOrders = numOrders;
    }
}
