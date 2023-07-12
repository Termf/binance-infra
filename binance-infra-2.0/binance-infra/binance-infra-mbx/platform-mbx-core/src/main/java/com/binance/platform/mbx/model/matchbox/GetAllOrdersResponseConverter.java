package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetAllOrdersResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/4 7:26 下午
 */
public class GetAllOrdersResponseConverter {
    public static GetAllOrdersResponse convert(MbxGetAllOrdersResponse mbxGetAllOrdersResponse) {
        GetAllOrdersResponse getAllOrdersResponse = new GetAllOrdersResponse();
        getAllOrdersResponse.setSymbol(mbxGetAllOrdersResponse.getSymbol());
        getAllOrdersResponse.setOrderId(mbxGetAllOrdersResponse.getOrderId());
        getAllOrdersResponse.setOrderListId(mbxGetAllOrdersResponse.getOrderListId());
        getAllOrdersResponse.setClientOrderId(mbxGetAllOrdersResponse.getClientOrderId());
        getAllOrdersResponse.setPrice(mbxGetAllOrdersResponse.getPrice());
        getAllOrdersResponse.setOrigQty(mbxGetAllOrdersResponse.getOrigQty());
        getAllOrdersResponse.setExecutedQty(mbxGetAllOrdersResponse.getExecutedQty());
        getAllOrdersResponse.setCummulativeQuoteQty(mbxGetAllOrdersResponse.getCummulativeQuoteQty());
        getAllOrdersResponse.setStatus(mbxGetAllOrdersResponse.getStatus());
        getAllOrdersResponse.setTimeInForce(mbxGetAllOrdersResponse.getTimeInForce());
        getAllOrdersResponse.setType(mbxGetAllOrdersResponse.getType());
        getAllOrdersResponse.setSide(mbxGetAllOrdersResponse.getSide());
        getAllOrdersResponse.setStopPrice(mbxGetAllOrdersResponse.getStopPrice());
        getAllOrdersResponse.setIcebergQty(mbxGetAllOrdersResponse.getIcebergQty());
        getAllOrdersResponse.setTime(mbxGetAllOrdersResponse.getTime());
        getAllOrdersResponse.setUpdateTime(mbxGetAllOrdersResponse.getUpdateTime());
        getAllOrdersResponse.setIsWorking(mbxGetAllOrdersResponse.isIsWorking());
        getAllOrdersResponse.setOrigQuoteOrderQty(mbxGetAllOrdersResponse.getOrigQuoteOrderQty());
        return getAllOrdersResponse;
    }
}
