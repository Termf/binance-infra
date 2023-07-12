package com.binance.master.old.models.userAsset;

public class UserAssetKey {
    private String uid;

    private String asset;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset == null ? null : asset.trim();
    }
}
