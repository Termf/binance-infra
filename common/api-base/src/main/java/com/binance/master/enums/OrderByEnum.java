package com.binance.master.enums;

public enum OrderByEnum implements BaseEnum {
    ASC("asc", "正序"),
    DESC("desc", "倒序"),;
    private String code;

    private String desc;

    OrderByEnum(String code, String desc) {
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
