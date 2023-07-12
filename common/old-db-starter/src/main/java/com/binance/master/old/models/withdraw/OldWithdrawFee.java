package com.binance.master.old.models.withdraw;

import java.math.BigDecimal;
import java.util.Date;

public class OldWithdrawFee {
    private Date date;

    private String asset;

    private BigDecimal txFee;

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

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }
}