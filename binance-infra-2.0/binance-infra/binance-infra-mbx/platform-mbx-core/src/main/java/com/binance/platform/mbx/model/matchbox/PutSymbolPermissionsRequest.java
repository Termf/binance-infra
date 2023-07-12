package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class PutSymbolPermissionsRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;

    private Boolean allowSpot;

    private Boolean allowMargin;

    private Integer permissionsBitmask;

    public PutSymbolPermissionsRequest(@NotEmpty String symbol) {
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