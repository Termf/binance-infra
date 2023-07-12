package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetCommissionResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 4:50 上午
 */
public class GetCommissionResponseConverter {
    public static GetCommissionResponse convert(MbxGetCommissionResponse mbxGetCommissionResponse) {
        GetCommissionResponse getCommissionResponse = new GetCommissionResponse();
        getCommissionResponse.setAsset(mbxGetCommissionResponse.getAsset());
        getCommissionResponse.setCommission(mbxGetCommissionResponse.getCommission());
        return getCommissionResponse;
    }
}
