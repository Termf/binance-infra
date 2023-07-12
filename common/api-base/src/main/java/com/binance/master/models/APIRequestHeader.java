package com.binance.master.models;

import com.binance.master.commons.ToString;
import com.binance.master.enums.LanguageEnum;
import com.binance.master.enums.TerminalEnum;
import com.binance.master.utils.TrackingUtils;

import io.swagger.annotations.ApiModelProperty;

public class APIRequestHeader extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = 9054983384754655361L;
    
    public static final APIRequestHeader HEADER_DEFAULT = new APIRequestHeader(LanguageEnum.EN_US, TerminalEnum.OTHER, null, null, null);

    /**
     * 语言，该字段准备废弃，一切以header中的lang为准，跟前端保持一致，没必要每加一个语言后端再做一次映射
     */
    @ApiModelProperty(value = "系统语言")
    private LanguageEnum language;
    /**
     * 设备类型，该字段准备废弃，一切以header中的clienttype为准，跟前端保持一致，后端没必要再维护一套映射关系
     */
    @ApiModelProperty(value = "终端")
    private TerminalEnum terminal;

    @ApiModelProperty(value = "版本号", required = false)
    private String version;

    /**
     * 授权令牌
     */
    @ApiModelProperty(value = "授权令牌", hidden = true)
    private String token;// 隐藏属性登录后必须传输

    @ApiModelProperty(value = "跟踪链", required = false)
    private String trackingChain;

    @ApiModelProperty(value = "域名", required = false)
    private String domain;


    public APIRequestHeader() {
        this.trackingChain = TrackingUtils.getTrackingChain();
    }
    
    public APIRequestHeader(LanguageEnum language, TerminalEnum terminal, String version, String token,String domain) {
        super();
        this.language = language;
        this.terminal = terminal;
        this.version = version;
        this.token = token;
        this.trackingChain = TrackingUtils.getTrackingChain();
        this.domain = domain;
    }


    public LanguageEnum getLanguage() {
        return language;
    }

    public void setLanguage(LanguageEnum language) {
        this.language = language;
    }

    public TerminalEnum getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalEnum terminal) {
        this.terminal = terminal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrackingChain() {
        return trackingChain;
    }

    public void setTrackingChain(String trackingChain) {
        this.trackingChain = trackingChain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
