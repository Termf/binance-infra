package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetTradesResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:25 上午
 */
public class GetTradesResponseConverter {
    public static GetTradesResponse convert(MbxGetTradesResponse mbxGetTradesResponse) {
        GetTradesResponse getTradesResponse = new GetTradesResponse();
        getTradesResponse.setSymbol(mbxGetTradesResponse.getSymbol());
        getTradesResponse.setId(mbxGetTradesResponse.getId());
        getTradesResponse.setBuyerAcctId(mbxGetTradesResponse.getBuyerAcctId());
        getTradesResponse.setSellerAcctId(mbxGetTradesResponse.getSellerAcctId());
        getTradesResponse.setBuyerOrderId(mbxGetTradesResponse.getBuyerOrderId());
        getTradesResponse.setSellerOrderId(mbxGetTradesResponse.getSellerOrderId());
        getTradesResponse.setBuyerOrderListId(mbxGetTradesResponse.getBuyerOrderListId());
        getTradesResponse.setSellerOrderListId(mbxGetTradesResponse.getSellerOrderListId());
        getTradesResponse.setBuyCommission(mbxGetTradesResponse.getBuyCommission());
        getTradesResponse.setBuyCommissionAsset(mbxGetTradesResponse.getBuyCommissionAsset());
        getTradesResponse.setSellCommission(mbxGetTradesResponse.getSellCommission());
        getTradesResponse.setSellCommissionAsset(mbxGetTradesResponse.getSellCommissionAsset());
        getTradesResponse.setPrice(mbxGetTradesResponse.getPrice());
        getTradesResponse.setQty(mbxGetTradesResponse.getQty());
        getTradesResponse.setTime(mbxGetTradesResponse.getTime());
        getTradesResponse.setIsBuyerMaker(mbxGetTradesResponse.isIsBuyerMaker());
        getTradesResponse.setIsBestMatch(mbxGetTradesResponse.isIsBestMatch());
        getTradesResponse.setQuoteQty(mbxGetTradesResponse.getQuoteQty());
        getTradesResponse.setMsgAuth(mbxGetTradesResponse.getMsgAuth());
        return getTradesResponse;
    }
}
