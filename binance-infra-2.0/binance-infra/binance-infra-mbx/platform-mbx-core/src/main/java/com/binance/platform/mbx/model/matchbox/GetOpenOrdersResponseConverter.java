package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOpenOrdersResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 12:56 上午
 */
public class GetOpenOrdersResponseConverter {
    public static GetOpenOrdersResponse convert(MbxGetOpenOrdersResponse mbxGetOpenOrdersResponse) {
        GetOpenOrdersResponse getOpenOrdersResponse = new GetOpenOrdersResponse();
        getOpenOrdersResponse.setSymbol(mbxGetOpenOrdersResponse.getSymbol());
        getOpenOrdersResponse.setOrderId(mbxGetOpenOrdersResponse.getOrderId());
        getOpenOrdersResponse.setOrderListId(mbxGetOpenOrdersResponse.getOrderListId());
        getOpenOrdersResponse.setClientOrderId(mbxGetOpenOrdersResponse.getClientOrderId());
        getOpenOrdersResponse.setPrice(mbxGetOpenOrdersResponse.getPrice());
        getOpenOrdersResponse.setOrigQty(mbxGetOpenOrdersResponse.getOrigQty());
        getOpenOrdersResponse.setExecutedQty(mbxGetOpenOrdersResponse.getExecutedQty());
        getOpenOrdersResponse.setCummulativeQuoteQty(mbxGetOpenOrdersResponse.getCummulativeQuoteQty());
        getOpenOrdersResponse.setStatus(mbxGetOpenOrdersResponse.getStatus());
        getOpenOrdersResponse.setTimeInForce(mbxGetOpenOrdersResponse.getTimeInForce());
        getOpenOrdersResponse.setType(mbxGetOpenOrdersResponse.getType());
        getOpenOrdersResponse.setSide(mbxGetOpenOrdersResponse.getSide());
        getOpenOrdersResponse.setStopPrice(mbxGetOpenOrdersResponse.getStopPrice());
        getOpenOrdersResponse.setIcebergQty(mbxGetOpenOrdersResponse.getIcebergQty());
        getOpenOrdersResponse.setTime(mbxGetOpenOrdersResponse.getTime());
        getOpenOrdersResponse.setUpdateTime(mbxGetOpenOrdersResponse.getUpdateTime());
        getOpenOrdersResponse.setIsWorking(mbxGetOpenOrdersResponse.isIsWorking());
        getOpenOrdersResponse.setOrigQuoteOrderQty(mbxGetOpenOrdersResponse.getOrigQuoteOrderQty());
        return getOpenOrdersResponse;
    }
}
