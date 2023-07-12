package com.binance.master.old.models.product;

import lombok.Data;

@Data
public class SymbolMeta {
    /**
     * 交易对
     * */
    private String symbol;
    /**
     * 类型
     * */
    private String symbolType;
    /**
     * 基本币
     * */
    private String baseAsset;
    /**
     * 标价币
     * */
    private String quoteAsset;
    /**
     * 状态
     * */
    private String status;
    /**
     * 测试标示
     * */
    private Boolean test;
    /**
     * 测试标示
     * */
    private Boolean active;
}
