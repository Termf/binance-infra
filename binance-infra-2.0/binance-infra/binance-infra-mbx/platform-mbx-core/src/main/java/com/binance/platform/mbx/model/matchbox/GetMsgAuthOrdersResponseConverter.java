package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetMsgAuthOrdersResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 1:30 上午
 */
public class GetMsgAuthOrdersResponseConverter {
    public static GetMsgAuthOrdersResponse convert(MbxGetMsgAuthOrdersResponse mbxGetMsgAuthOrdersResponse) {
        GetMsgAuthOrdersResponse getMsgAuthOrdersResponse = new GetMsgAuthOrdersResponse();
        getMsgAuthOrdersResponse.setSymbol(mbxGetMsgAuthOrdersResponse.getSymbol());
        getMsgAuthOrdersResponse.setOrderId(mbxGetMsgAuthOrdersResponse.getOrderId());
        getMsgAuthOrdersResponse.setOrderListId(mbxGetMsgAuthOrdersResponse.getOrderListId());
        getMsgAuthOrdersResponse.setClientOrderId(mbxGetMsgAuthOrdersResponse.getClientOrderId());
        getMsgAuthOrdersResponse.setPrice(mbxGetMsgAuthOrdersResponse.getPrice());
        getMsgAuthOrdersResponse.setOrigQty(mbxGetMsgAuthOrdersResponse.getOrigQty());
        getMsgAuthOrdersResponse.setExecutedQty(mbxGetMsgAuthOrdersResponse.getExecutedQty());
        getMsgAuthOrdersResponse.setCummulativeQuoteQty(mbxGetMsgAuthOrdersResponse.getCummulativeQuoteQty());
        getMsgAuthOrdersResponse.setStatus(mbxGetMsgAuthOrdersResponse.getStatus());
        getMsgAuthOrdersResponse.setTimeInForce(mbxGetMsgAuthOrdersResponse.getTimeInForce());
        getMsgAuthOrdersResponse.setType(mbxGetMsgAuthOrdersResponse.getType());
        getMsgAuthOrdersResponse.setSide(mbxGetMsgAuthOrdersResponse.getSide());
        getMsgAuthOrdersResponse.setStopPrice(mbxGetMsgAuthOrdersResponse.getStopPrice());
        getMsgAuthOrdersResponse.setIcebergQty(mbxGetMsgAuthOrdersResponse.getIcebergQty());
        getMsgAuthOrdersResponse.setTime(mbxGetMsgAuthOrdersResponse.getTime());
        getMsgAuthOrdersResponse.setUpdateTime(mbxGetMsgAuthOrdersResponse.getUpdateTime());
        getMsgAuthOrdersResponse.setIsWorking(mbxGetMsgAuthOrdersResponse.isIsWorking());
        getMsgAuthOrdersResponse.setOrigQuoteOrderQty(mbxGetMsgAuthOrdersResponse.getOrigQuoteOrderQty());
        return getMsgAuthOrdersResponse;
    }
}
