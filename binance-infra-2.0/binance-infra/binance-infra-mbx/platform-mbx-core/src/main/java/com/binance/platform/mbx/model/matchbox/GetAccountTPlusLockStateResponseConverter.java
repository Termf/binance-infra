package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAccountTPlusLockStateResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:27 上午
 */
public class GetAccountTPlusLockStateResponseConverter {
    public static GetAccountTPlusLockStateResponse convert(MbxGetAccountTPlusLockStateResponse mbxGetAccountTPlusLockStateResponse) {
        GetAccountTPlusLockStateResponse getAccountTPlusLockStateResponse = new GetAccountTPlusLockStateResponse();
        getAccountTPlusLockStateResponse.setAccountId(mbxGetAccountTPlusLockStateResponse.getAccountId());
        getAccountTPlusLockStateResponse.setSymbol(mbxGetAccountTPlusLockStateResponse.getSymbol());
        getAccountTPlusLockStateResponse.setAsset(mbxGetAccountTPlusLockStateResponse.getAsset());
        getAccountTPlusLockStateResponse.setUnlockData(mbxGetAccountTPlusLockStateResponse.getUnlockData());
        return getAccountTPlusLockStateResponse;
    }
}
