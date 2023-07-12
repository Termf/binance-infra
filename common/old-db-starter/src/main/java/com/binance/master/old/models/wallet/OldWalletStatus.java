package com.binance.master.old.models.wallet;

import java.util.Date;

public class OldWalletStatus {
    private Long id;

    private String coin;

    private String balance;

    private String unspentBalance;

    private String lastSuccessTime;

    private Date insertTime;

    private String clientName;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin == null ? null : coin.trim();
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance == null ? null : balance.trim();
    }

    public String getUnspentBalance() {
        return unspentBalance;
    }

    public void setUnspentBalance(String unspentBalance) {
        this.unspentBalance = unspentBalance == null ? null : unspentBalance.trim();
    }

    public String getLastSuccessTime() {
        return lastSuccessTime;
    }

    public void setLastSuccessTime(String lastSuccessTime) {
        this.lastSuccessTime = lastSuccessTime == null ? null : lastSuccessTime.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}