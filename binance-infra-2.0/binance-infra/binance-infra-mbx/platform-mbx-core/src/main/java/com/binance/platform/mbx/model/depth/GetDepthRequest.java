package com.binance.platform.mbx.model.depth;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

/**
 * 获取深度: 注意: limit=0 返回全部orderbook，但数据量会非常非常非常非常大！
 */
public class GetDepthRequest extends ToString {
    /** 产品代码 */
    @NotNull
    private String symbol;

    public GetDepthRequest(@NotNull String symbol) {
        this.symbol = symbol;
    }

    private Integer limit = 100;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getLimit() {
        return limit;
    }

    /** 默认 100; 最大 1000. 可选值:[5, 10, 20, 50, 100, 500, 1000] */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}