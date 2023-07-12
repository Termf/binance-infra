package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

public class DeleteTPlusSellFilterRequest extends ToString {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String symbol;
    private String force;

    public DeleteTPlusSellFilterRequest(@NotEmpty String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }
}