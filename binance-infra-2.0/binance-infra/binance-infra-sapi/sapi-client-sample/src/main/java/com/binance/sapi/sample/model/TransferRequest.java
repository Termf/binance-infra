package com.binance.sapi.sample.model;

/**
 * binance-sample
 *
 * @author Thomas Li
 * Date: 2021/7/16
 */
public class TransferRequest {

    private Double amount;

    private String asset;

    private String type;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
