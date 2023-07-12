package com.binance.master.enums;

import java.util.HashMap;
import java.util.Map;

public enum TerminalEnum implements BaseEnum {

    WEB("web", "web"),
    // 移动端 无法区分是ios 还是 android是显示
    MOBILE("mobile", "mobile"),
    IOS("ios", "ios"),
    ANDROID("android", "android"),
    // 为info临时添加终端
    IOSINFO("iosinfo", "iosinfo"),
    ANDROIDINFO("androidinfo", "androidinfo"),
    PC("pc", "pc"),
    H5("h5", "h5"),
    MAC("mac", "mac"),
    OTHER("other", "其他"),
    ELECTRON("electron","electron"),;

    static final Map<String, TerminalEnum> codeEnumMap = new HashMap<>();
    static{
        TerminalEnum[] terminalEnums = TerminalEnum.values();
        for (TerminalEnum terminalEnum : terminalEnums) {
            codeEnumMap.put(terminalEnum.getCode(), terminalEnum);
        }
    }

    private String code;

    private String desc;

    TerminalEnum(String code, String desc) {
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

    public static TerminalEnum findByCode(String code) {
        TerminalEnum result = codeEnumMap.get(code);
        return result!=null?result:null;
    }
}
