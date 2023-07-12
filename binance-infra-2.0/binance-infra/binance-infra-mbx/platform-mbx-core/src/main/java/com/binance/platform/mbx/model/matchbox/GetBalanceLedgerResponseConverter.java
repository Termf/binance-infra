package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetBalanceLedgerResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 11:42 下午
 */
public class GetBalanceLedgerResponseConverter {
    public static GetBalanceLedgerResponse convert(MbxGetBalanceLedgerResponse mbxGetBalanceLedgerResponse) {
        GetBalanceLedgerResponse getBalanceLedgerResponse = new GetBalanceLedgerResponse();
        getBalanceLedgerResponse.setAccountId(mbxGetBalanceLedgerResponse.getAccountId());
        getBalanceLedgerResponse.setAsset(mbxGetBalanceLedgerResponse.getAsset());
        getBalanceLedgerResponse.setBalanceDelta(mbxGetBalanceLedgerResponse.getBalanceDelta());
        getBalanceLedgerResponse.setTime(mbxGetBalanceLedgerResponse.getTime());
        getBalanceLedgerResponse.setUpdateId(mbxGetBalanceLedgerResponse.getUpdateId());
        getBalanceLedgerResponse.setExternalUpdateId(mbxGetBalanceLedgerResponse.getExternalUpdateId());
        getBalanceLedgerResponse.setMbxUpdateId(mbxGetBalanceLedgerResponse.getMbxUpdateId());
        return getBalanceLedgerResponse;
    }
}
