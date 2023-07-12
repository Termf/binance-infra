package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotNull;

/**
 * 查询open order Request.
 *
 */
public class QueryOpenOrderRequest extends ToString {
    private static final long serialVersionUID = -367928525540616853L;
    @NotNull
    private Long userId;
    /** 产品代码 */
    private String symbol;
    /** @deprecated */
    @Deprecated
    /** 1-下载,其他值或不传值-查询 */
    private String converter;

    public QueryOpenOrderRequest() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getSymbol() {
        return this.symbol;
    }

    /** @deprecated */
    @Deprecated
    public String getConverter() {
        return this.converter;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    /** @deprecated */
    @Deprecated
    public void setConverter(final String converter) {
        this.converter = converter;
    }
}