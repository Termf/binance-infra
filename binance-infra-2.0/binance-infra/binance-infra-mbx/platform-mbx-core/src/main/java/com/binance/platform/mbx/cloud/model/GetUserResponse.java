package com.binance.platform.mbx.cloud.model;

import com.binance.master.utils.StringUtils;

import java.io.Serializable;

/**
 * @author Li Fenggang
 * Date: 2020/7/13 2:04 下午
 */
public class GetUserResponse implements Serializable {
    private static final long serialVersionUID = 8709805917749346913L;
    private UserVo user;
    private UserSecurityVo userSecurity;
    private UserInfoVo userInfo;

    public String toString() {
        return StringUtils.objectToString(this);
    }

    public GetUserResponse(final UserVo user, final UserSecurityVo userSecurity, final UserInfoVo userInfo) {
        this.user = user;
        this.userSecurity = userSecurity;
        this.userInfo = userInfo;
    }

    public UserVo getUser() {
        return this.user;
    }

    public UserSecurityVo getUserSecurity() {
        return this.userSecurity;
    }

    public UserInfoVo getUserInfo() {
        return this.userInfo;
    }

    public void setUser(final UserVo user) {
        this.user = user;
    }

    public void setUserSecurity(final UserSecurityVo userSecurity) {
        this.userSecurity = userSecurity;
    }

    public void setUserInfo(final UserInfoVo userInfo) {
        this.userInfo = userInfo;
    }

    public GetUserResponse() {
    }
}
