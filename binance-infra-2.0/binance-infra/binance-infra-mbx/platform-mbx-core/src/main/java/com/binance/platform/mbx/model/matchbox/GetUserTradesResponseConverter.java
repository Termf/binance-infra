package com.binance.platform.mbx.model.matchbox;

import com.binance.platform.mbx.matchbox.model.mgmt.MbxGetUserTradesResponse;

/**
 * @author Li Fenggang
 * Date: 2020/8/5 5:01 上午
 */
public class GetUserTradesResponseConverter {
    public static GetUserTradesResponse convert(MbxGetUserTradesResponse mbxGetUserTradesResponse) {
        GetUserTradesResponse getUserTradesResponse = new GetUserTradesResponse();
        getUserTradesResponse.setSymbol(mbxGetUserTradesResponse.getSymbol());
        getUserTradesResponse.setId(mbxGetUserTradesResponse.getId());
        getUserTradesResponse.setBuyerAcctId(mbxGetUserTradesResponse.getBuyerAcctId());
        getUserTradesResponse.setSellerAcctId(mbxGetUserTradesResponse.getSellerAcctId());
        getUserTradesResponse.setBuyerOrderId(mbxGetUserTradesResponse.getBuyerOrderId());
        getUserTradesResponse.setSellerOrderId(mbxGetUserTradesResponse.getSellerOrderId());
        getUserTradesResponse.setBuyerOrderListId(mbxGetUserTradesResponse.getBuyerOrderListId());
        getUserTradesResponse.setSellerOrderListId(mbxGetUserTradesResponse.getSellerOrderListId());
        getUserTradesResponse.setBuyCommission(mbxGetUserTradesResponse.getBuyCommission());
        getUserTradesResponse.setBuyCommissionAsset(mbxGetUserTradesResponse.getBuyCommissionAsset());
        getUserTradesResponse.setSellCommission(mbxGetUserTradesResponse.getSellCommission());
        getUserTradesResponse.setSellCommissionAsset(mbxGetUserTradesResponse.getSellCommissionAsset());
        getUserTradesResponse.setPrice(mbxGetUserTradesResponse.getPrice());
        getUserTradesResponse.setQty(mbxGetUserTradesResponse.getQty());
        getUserTradesResponse.setTime(mbxGetUserTradesResponse.getTime());
        getUserTradesResponse.setIsBuyerMaker(mbxGetUserTradesResponse.isIsBuyerMaker());
        getUserTradesResponse.setIsBestMatch(mbxGetUserTradesResponse.isIsBestMatch());
        getUserTradesResponse.setQuoteQty(mbxGetUserTradesResponse.getQuoteQty());
        return getUserTradesResponse;
    }
}
