package com.binance.master.models;

import java.util.Date;

import com.binance.master.commons.ToString;

public class RedisVerify extends ToString {

    /**
     * 
     */
    private static final long serialVersionUID = -6805208243751266726L;

    private String token;

    private String code;

    private Date time;

    private Integer errorCount = 0;

    private Boolean isInvalid = false;

    private Long errorTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Boolean getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Boolean isInvalid) {
        this.isInvalid = isInvalid;
    }

    public Long getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Long errorTime) {
        this.errorTime = errorTime;
    }

}
