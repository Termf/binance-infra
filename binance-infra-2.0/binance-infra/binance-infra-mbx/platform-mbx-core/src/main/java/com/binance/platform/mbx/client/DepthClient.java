package com.binance.platform.mbx.client;

import com.binance.platform.mbx.matchbox.model.MbxResponse;
import com.binance.platform.mbx.model.depth.DepthResponse;
import com.binance.platform.mbx.model.depth.GetDepthRequest;

public interface DepthClient {

    /**
     * 获取深度信息，注意: limit=0 返回全部orderbook，但数据量会非常非常非常非常大！
     *
     * @param request
     * @return
     */
    MbxResponse<DepthResponse> getDepth(GetDepthRequest request);

}
