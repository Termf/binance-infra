package com.binance.platform.mbx.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAssetsResponse;
import com.binance.platform.mbx.model.matchbox.GetAssetsResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:12 上午
 */
public class GetAssetsResponseConverter {
    public static GetAssetsResponse convert(MbxGetAssetsResponse mbxGetAssetsResponse) {
        GetAssetsResponse getAssetsResponse = new GetAssetsResponse();
        getAssetsResponse.setAsset(mbxGetAssetsResponse.getAsset());
        getAssetsResponse.setDecimalPlaces(mbxGetAssetsResponse.getDecimalPlaces());
        return getAssetsResponse;
    }
}
