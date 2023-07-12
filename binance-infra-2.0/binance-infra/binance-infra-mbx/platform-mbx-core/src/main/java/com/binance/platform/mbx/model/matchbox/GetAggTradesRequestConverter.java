package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.matchbox.model.rest.RestGetAggTradesRequest;
import com.binance.platform.mbx.model.trade.GetAggTradesRequest;

public class GetAggTradesRequestConverter extends ToString {
    public static RestGetAggTradesRequest convert(GetAggTradesRequest source) {
        RestGetAggTradesRequest restGetAggTradesRequest = new RestGetAggTradesRequest();
        restGetAggTradesRequest.setSymbol(source.getSymbol());
        restGetAggTradesRequest.setStartTime(source.getStartTime());
        restGetAggTradesRequest.setEndTime(source.getEndTime());
        restGetAggTradesRequest.setLimit(source.getLimit());
        return restGetAggTradesRequest;
    }
}
