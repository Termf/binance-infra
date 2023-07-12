package com.binance.master.old.models.account;

import com.binance.master.commons.ToString;

public class OldUserIpKey extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = -3209689465727419685L;

    private String userid;

    private String ip;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }
}
