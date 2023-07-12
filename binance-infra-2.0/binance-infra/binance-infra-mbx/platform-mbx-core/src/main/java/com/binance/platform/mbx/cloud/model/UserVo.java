package com.binance.platform.mbx.cloud.model;

import com.binance.master.utils.StringUtils;
import java.io.Serializable;
import java.util.Date;

public class UserVo implements Serializable {
    private static final long serialVersionUID = -7264586240498718720L;
    private Long userId;
    private String email;
    private String password;
    private String salt;
    private Long status;
    private Date insertTime;
    private Date updateTime;

    public UserVo() {
    }

    public String toString() {
        return StringUtils.objectToString(this);
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSalt() {
        return this.salt;
    }

    public Long getStatus() {
        return this.status;
    }

    public Date getInsertTime() {
        return this.insertTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public void setStatus(final Long status) {
        this.status = status;
    }

    public void setInsertTime(final Date insertTime) {
        this.insertTime = insertTime;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }
}