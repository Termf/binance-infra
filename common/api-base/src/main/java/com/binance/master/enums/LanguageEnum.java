package com.binance.master.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 语言
 */
@Deprecated
public enum LanguageEnum implements BaseEnum {
    EN_US("en", "en_US", "英语(美国)"),
    ZH_CN("cn", "zh_CN", "简体中文(中国)"),
    DE_DE("de", "de_DE", "德语(德国)"),
    KO_KR("kr", "ko_KR", "韩文(韩国)"),
    ZH_TW("tw", "zh_TW", "繁体中文(台湾地区)"),
    RU_RU("ru", "ru_RU", "俄罗斯"),
    ES_ES("es", "ES_ES", "西班牙文");

    private String lang;
    private String code;
    private String desc;

    LanguageEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    LanguageEnum(String lang, String code, String desc) {
        this.lang = lang;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static LanguageEnum findByCode(String code) {
        if (StringUtils.equalsIgnoreCase(code, "zh-CN")) {
            return LanguageEnum.ZH_CN;
        }
        LanguageEnum[] languageEnums = LanguageEnum.values();
        for (LanguageEnum languageEnum : languageEnums) {
            if (languageEnum.getCode().equals(code)) {
                return languageEnum;
            }
        }
        return null;
    }

    public static LanguageEnum findByLang(String lang) {
        if (StringUtils.equalsIgnoreCase(lang, "zh-CN")) {
            return LanguageEnum.ZH_CN;
        }
        LanguageEnum[] languageEnums = LanguageEnum.values();
        for (LanguageEnum languageEnum : languageEnums) {
            if (languageEnum.getLang().equals(lang)) {
                return languageEnum;
            }
        }
        return LanguageEnum.EN_US;
    }
}
