package com.binance.master.old.models.account;

import java.util.Date;

import com.binance.master.commons.ToString;

public class OldUserPasswordLog extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 4001985492008801092L;

    private Integer id;

    private String ip;

    private String userid;

    private Date time;

    private Integer result;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
