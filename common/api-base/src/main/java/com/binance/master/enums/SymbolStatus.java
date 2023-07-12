package com.binance.master.enums;

public enum SymbolStatus implements BaseEnum {
    PRE_TRADING,
    TRADING,
    POST_TRADING,
    END_OF_DAY,
    HALT,
    BREAK,
    AUCTION_MATCH,
    DELIVER;

    private String code;

    private String desc;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
