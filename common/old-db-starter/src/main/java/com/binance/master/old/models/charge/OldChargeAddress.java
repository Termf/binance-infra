package com.binance.master.old.models.charge;

import java.io.Serializable;
import java.util.Date;

public class OldChargeAddress implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6685581640312802451L;

    private String address;

    private String addressTag;

    private String coin;

    private String checkSum;

    private String status;

    private Date createTime;

    private Date modifyTime;

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

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin == null ? null : coin.trim();
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum == null ? null : checkSum.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
