package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetLedgerResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 3:37 上午
 */
public class GetAssetLedgerResponseConverter {
    public static GetAssetLedgerResponse convert(MbxGetAssetLedgerResponse mbxGetAssetLedgerResponse) {
        GetAssetLedgerResponse getAssetLedgerResponse = new GetAssetLedgerResponse();
        getAssetLedgerResponse.setAccountId(mbxGetAssetLedgerResponse.getAccountId());
        getAssetLedgerResponse.setAsset(mbxGetAssetLedgerResponse.getAsset());
        getAssetLedgerResponse.setBalanceDelta(mbxGetAssetLedgerResponse.getBalanceDelta());
        getAssetLedgerResponse.setTime(mbxGetAssetLedgerResponse.getTime());
        getAssetLedgerResponse.setUpdateId(mbxGetAssetLedgerResponse.getUpdateId());
        getAssetLedgerResponse.setExternalUpdateId(mbxGetAssetLedgerResponse.getExternalUpdateId());
        getAssetLedgerResponse.setMbxUpdateId(mbxGetAssetLedgerResponse.getMbxUpdateId());
        return getAssetLedgerResponse;
    }
}
