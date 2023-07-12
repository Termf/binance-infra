package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetSymbolOrdersResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 9:55 下午
 */
public class GetSymbolOrdersResponseConverter {
    public static GetSymbolOrdersResponse convert(MbxGetSymbolOrdersResponse mbxGetSymbolOrdersResponse) {
        GetSymbolOrdersResponse getSymbolOrdersResponse = new GetSymbolOrdersResponse();
        getSymbolOrdersResponse.setSymbol(mbxGetSymbolOrdersResponse.getSymbol());
        getSymbolOrdersResponse.setOrderId(mbxGetSymbolOrdersResponse.getOrderId());
        getSymbolOrdersResponse.setOrderListId(mbxGetSymbolOrdersResponse.getOrderListId());
        getSymbolOrdersResponse.setClientOrderId(mbxGetSymbolOrdersResponse.getClientOrderId());
        getSymbolOrdersResponse.setPrice(mbxGetSymbolOrdersResponse.getPrice());
        getSymbolOrdersResponse.setOrigQty(mbxGetSymbolOrdersResponse.getOrigQty());
        getSymbolOrdersResponse.setExecutedQty(mbxGetSymbolOrdersResponse.getExecutedQty());
        getSymbolOrdersResponse.setCummulativeQuoteQty(mbxGetSymbolOrdersResponse.getCummulativeQuoteQty());
        getSymbolOrdersResponse.setStatus(mbxGetSymbolOrdersResponse.getStatus());
        getSymbolOrdersResponse.setTimeInForce(mbxGetSymbolOrdersResponse.getTimeInForce());
        getSymbolOrdersResponse.setType(mbxGetSymbolOrdersResponse.getType());
        getSymbolOrdersResponse.setSide(mbxGetSymbolOrdersResponse.getSide());
        getSymbolOrdersResponse.setStopPrice(mbxGetSymbolOrdersResponse.getStopPrice());
        getSymbolOrdersResponse.setIcebergQty(mbxGetSymbolOrdersResponse.getIcebergQty());
        getSymbolOrdersResponse.setTime(mbxGetSymbolOrdersResponse.getTime());
        getSymbolOrdersResponse.setUpdateTime(mbxGetSymbolOrdersResponse.getUpdateTime());
        getSymbolOrdersResponse.setIsWorking(mbxGetSymbolOrdersResponse.isIsWorking());
        getSymbolOrdersResponse.setOrigQuoteOrderQty(mbxGetSymbolOrdersResponse.getOrigQuoteOrderQty());
        return getSymbolOrdersResponse;
    }
}
