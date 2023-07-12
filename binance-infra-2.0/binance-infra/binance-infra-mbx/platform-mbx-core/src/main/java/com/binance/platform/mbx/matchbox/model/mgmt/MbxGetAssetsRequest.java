package com.binance.platform.mbx.matchbox.model.mgmt;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * @author Li Fenggang
 * Date: 2020/7/2 10:50 上午
 */
public class MbxGetAssetsRequest extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v1/assets";
    }
    
}
