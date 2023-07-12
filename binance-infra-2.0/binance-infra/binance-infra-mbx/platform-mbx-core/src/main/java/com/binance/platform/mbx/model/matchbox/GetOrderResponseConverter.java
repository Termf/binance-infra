package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetOrderResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 1:41 上午
 */
public class GetOrderResponseConverter {
    public static GetOrderResponse convert(MbxGetOrderResponse mbxGetOrderResponse) {
        GetOrderResponse getOrderResponse = new GetOrderResponse();
        getOrderResponse.setAccountId(mbxGetOrderResponse.getAccountId());
        getOrderResponse.setSymbol(mbxGetOrderResponse.getSymbol());
        getOrderResponse.setOrderId(mbxGetOrderResponse.getOrderId());
        getOrderResponse.setOrderListId(mbxGetOrderResponse.getOrderListId());
        getOrderResponse.setClientOrderId(mbxGetOrderResponse.getClientOrderId());
        getOrderResponse.setPrice(mbxGetOrderResponse.getPrice());
        getOrderResponse.setOrigQty(mbxGetOrderResponse.getOrigQty());
        getOrderResponse.setExecutedQty(mbxGetOrderResponse.getExecutedQty());
        getOrderResponse.setCummulativeQuoteQty(mbxGetOrderResponse.getCummulativeQuoteQty());
        getOrderResponse.setStatus(mbxGetOrderResponse.getStatus());
        getOrderResponse.setTimeInForce(mbxGetOrderResponse.getTimeInForce());
        getOrderResponse.setType(mbxGetOrderResponse.getType());
        getOrderResponse.setSide(mbxGetOrderResponse.getSide());
        getOrderResponse.setStopPrice(mbxGetOrderResponse.getStopPrice());
        getOrderResponse.setIcebergQty(mbxGetOrderResponse.getIcebergQty());
        getOrderResponse.setTime(mbxGetOrderResponse.getTime());
        getOrderResponse.setUpdateTime(mbxGetOrderResponse.getUpdateTime());
        getOrderResponse.setIsWorking(mbxGetOrderResponse.isIsWorking());
        getOrderResponse.setOrigQuoteOrderQty(mbxGetOrderResponse.getOrigQuoteOrderQty());
        return getOrderResponse;
    }
}
