package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

import javax.validation.constraints.NotEmpty;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxPutSymbolPermissionsRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/symbolPermissions";
    }

    @NotEmpty
    private String symbol;

    private Boolean allowSpot;

    private Boolean allowMargin;

    private Integer permissionsBitmask;

    public MbxPutSymbolPermissionsRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getAllowSpot() {
        return allowSpot;
    }

    public void setAllowSpot(Boolean allowSpot) {
        this.allowSpot = allowSpot;
    }

    public Boolean getAllowMargin() {
        return allowMargin;
    }

    public void setAllowMargin(Boolean allowMargin) {
        this.allowMargin = allowMargin;
    }

    public Integer getPermissionsBitmask() {
        return permissionsBitmask;
    }

    public void setPermissionsBitmask(Integer permissionsBitmask) {
        this.permissionsBitmask = permissionsBitmask;
    }
    
}
