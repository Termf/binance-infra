package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetEstimateSymbolResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 7:14 上午
 */
public class GetEstimateSymbolResponseConverter {
    public static GetEstimateSymbolResponse convert(MbxGetEstimateSymbolResponse mbxGetEstimateSymbolResponse) {
        GetEstimateSymbolResponse getEstimateSymbolResponse = new GetEstimateSymbolResponse();
        getEstimateSymbolResponse.setBaseDecimalPlaces(mbxGetEstimateSymbolResponse.getBaseDecimalPlaces());
        getEstimateSymbolResponse.setQuoteDecimalPlaces(mbxGetEstimateSymbolResponse.getQuoteDecimalPlaces());
        getEstimateSymbolResponse.setMaxPrice(mbxGetEstimateSymbolResponse.getMaxPrice());
        getEstimateSymbolResponse.setMaxQty(mbxGetEstimateSymbolResponse.getMaxQty());
        return getEstimateSymbolResponse;
    }
}
