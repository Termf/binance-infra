package com.binance.master.old.models.report;

import java.math.BigDecimal;
import java.util.Date;

public class OldExchangeFee {
    private Date date;

    private String asset;

    private BigDecimal balance;

    private Long tranId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }
}