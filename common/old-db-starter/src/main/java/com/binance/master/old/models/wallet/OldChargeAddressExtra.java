package com.binance.master.old.models.wallet;

import java.util.Date;

public class OldChargeAddressExtra extends OldChargeAddressExtraKey {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}