package com.binance.platform.mbx.matchbox.model.rest;

import com.binance.platform.mbx.matchbox.model.MbxBaseRequest;

/**
 * Get the prices of all symbols
 *
 * @author Li Fenggang
 * Date: 2020/7/17 3:26 下午
 */
public class RestGetTickerPriceAllV3Request extends MbxBaseRequest {
    @Override
    public String getUri() {
        return "/v3/ticker/price";
    }
}
