package com.binance.master.old.models.charge;

public class OldBlockLogKey {
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

    public OldBlockLogKey() {
        super();
    }

    public OldBlockLogKey(Integer id, String coin) {
        super();
        this.id = id;
        this.coin = coin;
    }
    
}