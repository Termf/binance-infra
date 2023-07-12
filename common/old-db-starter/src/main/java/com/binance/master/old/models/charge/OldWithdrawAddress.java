package com.binance.master.old.models.charge;

import java.util.Date;

public class OldWithdrawAddress {
    private Integer id;

    private String userId;

    private String asset;

    private String name;

    private String address;

    private String addressTag;

    private String defaultFlag;

    private String status;

    private String checkSum;

    private Date createTime;

    private Date updateTime;

    private Boolean whiteStatus;

    private String uuid;

    private Date emailSendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAddressTag() {
        return addressTag;
    }

    public void setAddressTag(String addressTag) {
        this.addressTag = addressTag == null ? null : addressTag.trim();
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag == null ? null : defaultFlag.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum == null ? null : checkSum.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getWhiteStatus() {
        return whiteStatus;
    }

    public void setWhiteStatus(Boolean whiteStatus) {
        this.whiteStatus = whiteStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Date getEmailSendTime() {
        return emailSendTime;
    }

    public void setEmailSendTime(Date emailSendTime) {
        this.emailSendTime = emailSendTime;
    }
}