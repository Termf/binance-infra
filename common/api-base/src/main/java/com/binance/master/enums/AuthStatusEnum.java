package com.binance.master.enums;

public enum AuthStatusEnum implements BaseEnum {
    NOT_NEED_AUTH("notNeedAuth", "不需要认证"),
    NO_AUTH("notAuth", "未认证"),
    OK("ok", "认证成功"),;
    private String code;

    private String desc;

    AuthStatusEnum(String code, String desc) {
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
