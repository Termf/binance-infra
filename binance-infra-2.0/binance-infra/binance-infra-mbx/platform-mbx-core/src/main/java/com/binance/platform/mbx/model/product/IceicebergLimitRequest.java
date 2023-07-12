package com.binance.platform.mbx.model.product;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 设置冰山单拆分数量
 */
public class IceicebergLimitRequest extends ToString {
    /**
     *
     */
    private static final long serialVersionUID = 2573793525204222307L;
    /**
     * 交易对
     */
    @NotEmpty
    private String symbol;
    /**
     * 数量
     */
    @NotNull
    private Integer limit;

    public IceicebergLimitRequest(@NotEmpty String symbol, @NotNull Integer limit) {
        this.symbol = symbol;
        this.limit = limit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
