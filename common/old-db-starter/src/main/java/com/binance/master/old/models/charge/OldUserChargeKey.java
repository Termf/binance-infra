package com.binance.master.old.models.charge;

public class OldUserChargeKey {
    private Integer id;

    private String coin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin == null ? null : coin.trim();
    }
}