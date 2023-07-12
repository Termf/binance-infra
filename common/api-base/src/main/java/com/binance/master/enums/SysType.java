package com.binance.master.enums;

public enum SysType implements BaseEnum {
    PNK_WEB("pnk_web", "前台"),
    PNK_ADMIN("pnk_admin", "后台"),
    EXCHANGE_ADMIN("exchange_admin", "exchange-admin"),
    RISK("risk_exception_action_exchange", "risk"),
    ;
    private String code;

    private String desc;

    SysType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
