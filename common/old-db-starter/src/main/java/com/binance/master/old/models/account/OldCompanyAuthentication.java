package com.binance.master.old.models.account;

import java.util.Date;

import com.binance.master.commons.ToString;

public class OldCompanyAuthentication extends ToString {
    /**
     * 
     */
    private static final long serialVersionUID = 2212232141770345173L;

    private Long id;

    private String userId;

    private String companyName;

    private String companyAddress;

    private String companyCountry;


    private String applyerName;

    private String applyerEmail;

    private Integer status;

    private String info;

    private Date insertTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName == null ? null : applyerName.trim();
    }

    public String getApplyerEmail() {
        return applyerEmail;
    }

    public void setApplyerEmail(String applyerEmail) {
        this.applyerEmail = applyerEmail == null ? null : applyerEmail.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
