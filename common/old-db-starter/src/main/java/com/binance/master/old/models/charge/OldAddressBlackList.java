package com.binance.master.old.models.charge;

import java.util.Date;

public class OldAddressBlackList {
    private Integer id;

    private String address;

    private Integer type;

    private String remark;

    private Date time;

    private String userId;

    private String currency;

    private Date updateTime;

    private String updatedByAdminId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatedByAdminId() {
        return updatedByAdminId;
    }

    public void setUpdatedByAdminId(String updatedByAdminId) {
        this.updatedByAdminId = updatedByAdminId == null ? null : updatedByAdminId.trim();
    }
}