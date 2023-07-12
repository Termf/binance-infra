package com.binance.master.enums;

public enum AuthTypeEnum implements BaseEnum {

    GOOGLE("google", "谷歌"),
    SMS("sms", "短信"),
    FIDO2("fido2", "FIDO2"),
    FUND_PWD("fund-password","法币资金密码");

    private String code;
    private String desc;

    AuthTypeEnum(String code, String desc) {
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
