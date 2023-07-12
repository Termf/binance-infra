package com.binance.master.old.models.userAsset;

public class UserAssetLogWithdrawKey extends UserAssetKey {
    private Long userId;

    private String asset;

    private String tranId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public UserAssetLogWithdrawKey(Long userId, String asset, String tranId) {
        super();
        this.userId = userId;
        this.asset = asset;
        this.tranId = tranId;
    }


    public UserAssetLogWithdrawKey() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((asset == null) ? 0 : asset.hashCode());
        result = prime * result + ((tranId == null) ? 0 : tranId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserAssetLogWithdrawKey other = (UserAssetLogWithdrawKey) obj;
        if (asset == null) {
            if (other.asset != null)
                return false;
        } else if (!asset.equals(other.asset))
            return false;
        if (tranId == null) {
            if (other.tranId != null)
                return false;
        } else if (!tranId.equals(other.tranId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
