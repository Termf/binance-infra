package com.binance.master.old.models.wallet;

public class OldChargeAddressExtraKey {
    private String address;

    private String coin;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin == null ? null : coin.trim();
    }
}