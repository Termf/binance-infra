package com.binance.master.old.models.sys;

import java.util.Date;

public class TableMap {
    private Date begin;

    private Date end;

    private String postfix;

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix == null ? null : postfix.trim();
    }
}
