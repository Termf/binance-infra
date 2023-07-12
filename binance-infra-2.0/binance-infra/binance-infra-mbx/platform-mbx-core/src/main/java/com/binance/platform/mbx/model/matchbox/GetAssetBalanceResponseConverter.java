package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetBalanceResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:27 上午
 */
public class GetAssetBalanceResponseConverter {
    public static GetAssetBalanceResponse convert(MbxGetAssetBalanceResponse mbxResponseData) {
        GetAssetBalanceResponse getAssetBalanceResponse = new GetAssetBalanceResponse();
        getAssetBalanceResponse.setAsset(mbxResponseData.getAsset());
        getAssetBalanceResponse.setFree(mbxResponseData.getFree());
        getAssetBalanceResponse.setLocked(mbxResponseData.getLocked());
        getAssetBalanceResponse.setExtLocked(mbxResponseData.getExtLocked());
        return getAssetBalanceResponse;
    }
}
