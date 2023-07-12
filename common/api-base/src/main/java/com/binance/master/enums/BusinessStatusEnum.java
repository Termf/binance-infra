package com.binance.master.enums;

/**
 * 业务状态
 */
public enum BusinessStatusEnum implements BaseEnum {
    OK("ok", "正常"),
    ERROR("error", "异常"),;

    private String code;

    private String desc;

    BusinessStatusEnum(String code, String desc) {
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
